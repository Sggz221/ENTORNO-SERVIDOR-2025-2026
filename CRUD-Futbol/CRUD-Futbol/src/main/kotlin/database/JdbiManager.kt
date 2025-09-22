package org.example.database

import org.example.Storage.Storage
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.spi.JdbiPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.lighthousegames.logging.logging
import java.io.File

class JdbiManager(val isForTest: Boolean = false) {
    private val logger = logging()
    companion object {
        val instance: Jdbi = JdbiManager().jdbi
    }
    val urlNormal = "jdbc:h2:./jugadores"
    val urlTest = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val urlFinal = if(isForTest) urlNormal else urlTest

    val jdbi = Jdbi.create(urlFinal)

    init {
        logger.debug{ "Iniciando JdbiManager" }
        jdbi.installPlugin(KotlinPlugin())
        jdbi.installPlugin(SqlObjectPlugin())

        logger.debug { "Ejecutando script de creacion de tablas" }
        executeSqlScriptFromResources("tables.sql")
    }

    private fun executeSqlScriptFromResources(resourcePath: String){
        logger.debug { "Ejecutando script de ruta: $resourcePath" }
        val inputStream = ClassLoader.getSystemResourceAsStream(resourcePath)?.bufferedReader()!!
        val script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createUpdate(script).execute()
        }
    }
}