package org.example.mappers

import org.example.DAO.JugadorEntity
import org.example.DTO.JugadorDto
import org.example.Models.Club
import org.example.Models.Jugador
import org.example.Models.Posicion

fun JugadorEntity.toModel(): Jugador {
    return Jugador(
        id = this.id,
        dorsal = this.dorsal,
        nombre = this.nombre,
        posicion = Posicion.valueOf(this.posicion),
        club = Club.valueOf(this.club),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}
fun Jugador.toEntity(): JugadorEntity {
    return JugadorEntity(
        id = this.id,
        dorsal = this.dorsal,
        nombre = this.nombre,
        posicion = this.posicion.toString(),
        club = this.club.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
    )
}

fun JugadorDto.toModel(): Jugador {
    return Jugador(
        id = this.id,
        dorsal = this.dorsal,
        nombre = this.nombre,
        posicion = Posicion.valueOf(this.posicion),
        club = Club.valueOf(this.club)
    )
}

fun Jugador.toDto(): JugadorDto {
    return JugadorDto(
        id = this.id,
        dorsal = this.dorsal,
        nombre = this.nombre,
        posicion = this.posicion.toString(),
        club = this.club.toString()
    )
}