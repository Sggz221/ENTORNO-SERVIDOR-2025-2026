package org.example.Storage

import org.example.Errors.JugadorError
import com.github.michaelbull.result.Result
import org.example.Models.Jugador
import java.io.File

interface Storage {
    fun fileRead(file: File): Result<List<Jugador>, JugadorError>
    fun fileWrite(list: List<Jugador>, file: File): Result<Unit, JugadorError>
}