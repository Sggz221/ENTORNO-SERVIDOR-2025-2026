package org.example

import cache.darCacheJugadores
import dao.darPersonasDao
import org.example.Repository.JugadorRepositoryImpl
import org.example.Service.ServiceImpl
import org.example.Storage.StorageImpl
import org.example.Validator.Validator
import org.example.database.JdbiManager
import java.io.File


fun main() {
    val readFrom = File("data", "jugadores.csv")
    val service = ServiceImpl(
        repo = JugadorRepositoryImpl(darPersonasDao(JdbiManager.instance)),
        cache = darCacheJugadores(),
        validator = Validator(),
        storage = StorageImpl()
    )
    service.fileRead(readFrom)

    val jugadores = service.getAll().value

    jugadores.filter { it.club.toString() == "REAL_MADRID" }.forEach { println(it) }
    jugadores.groupBy { it.posicion }.forEach { println("${it.key}: ${it.value}") }
    println(jugadores.groupBy { it.club }.mapValues{ (_, value) -> value.groupingBy { it.posicion }.eachCount() })
}