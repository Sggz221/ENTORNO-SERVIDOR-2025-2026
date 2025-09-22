package org.example.Service

import com.github.michaelbull.result.Result
import org.example.Errors.JugadorError
import org.example.Models.Jugador

interface Service<Jugador, JugadorError, Long, File> {
    fun getAll(): Result<List<Jugador>, JugadorError>
    fun getById(id: Long): Result<Jugador, JugadorError>
    fun save(jugador: Jugador): Result<Jugador, JugadorError>
    fun update(id: Long, jugador: Jugador): Result<Jugador, JugadorError>
    fun delete(id: Long): Result<Jugador, JugadorError>
    fun fileRead(file: File): Result<List<Jugador>, JugadorError>
    fun fileWrite(file: File): Result<Unit, JugadorError>
    fun update(
        id: kotlin.Long,
        jugador: org.example.Models.Jugador
    ): Result<org.example.Models.Jugador, org.example.Errors.JugadorError>
}