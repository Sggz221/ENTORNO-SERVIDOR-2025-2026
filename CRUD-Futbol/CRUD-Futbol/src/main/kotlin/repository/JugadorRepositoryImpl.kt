package org.example.Repository

import dao.JugadorDao
import org.example.Models.Jugador
import org.example.mappers.toEntity
import org.example.mappers.toModel
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

class JugadorRepositoryImpl(
    private val dao: JugadorDao
): JugadorRepository {
    private val logger = logging()

    override fun getAll(): List<Jugador> {
        logger.debug { "Obteniendo todos los jugadores" }
        return dao.getAll().map { it.toModel() }
    }

    override fun getById(id: Long): Jugador? {
        logger.debug { "Obteniendo jguador por ID: $id" }
        return dao.getById(id)?.toModel()
    }

    override fun save(entity: Jugador): Jugador {
        val id = dao.save(entity.toEntity())
        val jugador: Jugador = entity.copy(id = id.toLong())
        return jugador
    }

    override fun delete(id: Long): Jugador? {
        logger.debug { "Borrando jguador por ID: $id" }
        dao.getById(id)?.let { if (dao.delete(id) == 1) return it.toModel() } // Como devuelve el numero de filas afectadas, si devuelve 1 sabremos que la operacion ha tenido exito
        return null
    }

    override fun update(id: Long, entity: Jugador): Jugador? {
        logger.debug { "Actualizando jguador con ID: ${entity.id}" }
        val toUpdate = dao.getById(id)
        if(toUpdate == null) {
            logger.debug { "Jugador no ecnontrado con id: $id" }
            return null
        }
        val timeStamp = LocalDateTime.now()
        val updated = entity.copy(updatedAt = timeStamp)
        dao.update(updated.toEntity(), id)
        return updated
    }
}