package org.example.Storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.DTO.JugadorDto
import org.example.Errors.JugadorError
import org.example.Models.Jugador
import org.example.mappers.toDto
import org.example.mappers.toModel
import java.io.File

class StorageImpl: Storage {
    override fun fileRead(file: File): Result<List<Jugador>, JugadorError> {
        if(!file.exists() || !file.canRead()) return Err(JugadorError.StorageError("El archivo no existe o no se puede leer"))

        return Ok(
            file.readLines()
                .drop(1)
                .map { it.split(",") }
                .map {
                    JugadorDto(
                        id = it[0].toLong(),
                        dorsal = it[1].toInt(),
                        nombre = it[2],
                        posicion = it[3],
                        club = it[4]
                    ).toModel()
                }
        )
    }

    override fun fileWrite(list: List<Jugador>, file:File): Result<Unit, JugadorError> {
        if(!file.parentFile.exists() || !file.parentFile.isDirectory) return Err(JugadorError.StorageError("El directorio padre no existe o no es un directorio."))

        file.writeText("id,dorsal,nombre,posicion,club")
        list.map{
            it.toDto()
            file.appendText("${it.id},${it.dorsal},${it.nombre},${it.posicion},${it.club}")
        }
        return Ok(Unit)
    }
}