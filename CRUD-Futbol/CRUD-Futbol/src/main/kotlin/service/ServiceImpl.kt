package org.example.Service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.*
import cache.darCacheJugadores
import dao.darPersonasDao
import org.example.Errors.JugadorError
import org.example.Models.Jugador
import org.example.Repository.JugadorRepositoryImpl
import org.example.Storage.StorageImpl
import org.example.Validator.Validator
import org.example.database.JdbiManager
import org.lighthousegames.logging.logging
import java.io.File

class ServiceImpl(
    private val repo: JugadorRepositoryImpl,
    private val cache: Cache<Long, Jugador>,
    private val validator: Validator,
    private val storage: StorageImpl,
    ): Service<Jugador, JugadorError, Long, File> {
        private val logger = logging()

    override fun getAll(): Result<List<Jugador>, JugadorError> {
        logger.debug { "Obteniendo todos los Jugadores" }
        return try {
            Ok(repo.getAll())
        }
        catch (e:Exception){
            Err(JugadorError.DatabaseError("Error de base de datos: ${e.message}"))
        }
    }

    override fun getById(id: Long): Result<Jugador, JugadorError> {
        return cache.getIfPresent(id)?.let { Ok(it) }?:run {
            try {
                repo.getById(id)?.let {
                    cache.put(id, it)
                    return Ok(it)
                }?:let{
                    return Err(JugadorError.NotFoundError("$id"))
                }
            }
            catch (e: Exception){
                return Err(JugadorError.DatabaseError("Error en la base de datos: ${e.message}"))
            }
        }
    }

    override fun fileWrite(file: File): Result<Unit, JugadorError> {
        logger.debug { "Exportando a fichero: ${file.path}" }
        val lista: List<Jugador>
        try {
            lista = repo.getAll()
        }
        catch (e:Exception){
            return Err(JugadorError.DatabaseError("Error de la base de datos: ${e.message}"))
        }
        val result = storage.fileWrite(lista, file)
        if (result.isOk) return Ok(Unit)
        else return Err(result.error)
    }

    override fun fileRead(file: File): Result<List<Jugador>, JugadorError> {
        logger.debug { "Importando de fichero: ${file.path}" }
        try {
            val lista = storage.fileRead(file)
            if(lista.isErr)return Err(lista.error)
            lista.value.forEach {
                val validado = validator.validate(it)
                if(validado.isErr) return Err(validado.error)
                repo.save(it)
            }
            return Ok(lista.value)
        }
        catch (e:Exception){
            return Err(JugadorError.DatabaseError("Error de la base de datos: ${e.message}"))
        }
    }

    override fun delete(id: Long): Result<Jugador, JugadorError> {
        logger.debug { "Borrando jugador con ID: ${id}" }
        val borrado = repo.delete(id) ?: return Err(JugadorError.DatabaseError("Jugador no existente con ID: ${id}"))
        cache.invalidate(id)
        return Ok(borrado)
    }

    override fun update(id: Long, jugador: Jugador): Result<Jugador, JugadorError> {
        logger.debug { "Actualizando jugador con ID: ${id}" }
        val validado = validator.validate(jugador)
        if(validado.isErr) return Err(validado.error)
        try {
            repo.update(id, jugador)?.let {
                cache.getIfPresent(id)?.let {
                    cache.invalidate(id)
                    cache.put(id, it)
                }
                return Ok(it)
            }?: return Err(JugadorError.NotFoundError("Jugador no encontrado para ID: $id"))
        }
        catch (e:Exception){
            return Err(JugadorError.DatabaseError("Error de la base de datos: ${e.message}"))
        }
    }

    override fun save(jugador: Jugador): Result<Jugador, JugadorError> {
        logger.debug { "Guardando Jugador" }
        val validado = validator.validate(jugador)
        if(validado.isErr) return Err(validado.error)
        try {
            return Ok(repo.save(jugador))
        }
        catch (e:Exception){
            return Err(JugadorError.DatabaseError("Error de la base de datos: ${e.message}"))
        }
    }
}