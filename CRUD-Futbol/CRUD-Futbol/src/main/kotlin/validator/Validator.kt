package org.example.Validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.Errors.JugadorError
import org.example.Models.Jugador
import org.lighthousegames.logging.logging

class Validator {
    private val logger = logging()

    fun validate(jugador: Jugador): Result<Jugador, JugadorError.InvalidoError>{
        logger.debug { "Validando jugador: $jugador" }
        if(jugador.nombre.length > 35 || jugador.nombre.isBlank()) return Err(JugadorError.InvalidoError("El nombre del jugador no puede ser mayor a 35 caracteres ni puede estar vacio."))
        if(jugador.dorsal < 1 || jugador.dorsal > 99) return Err(JugadorError.InvalidoError("La dorsal del jugador debe estar comprendida entre los numeros 1 y 99."))
        return Ok(jugador)
    }
}