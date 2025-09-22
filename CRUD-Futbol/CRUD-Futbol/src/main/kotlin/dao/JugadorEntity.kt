package org.example.DAO

import java.time.LocalDateTime

data class JugadorEntity(
    val id: Long,
    val dorsal: Int,
    val nombre: String,
    val posicion: String,
    val club: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
