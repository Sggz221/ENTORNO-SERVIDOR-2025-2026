package org.example.Models

import java.time.LocalDateTime

data class Jugador(
    val id: Long = 0L,
    val dorsal: Int,
    val nombre: String,
    val posicion: Posicion,
    val club: Club,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
