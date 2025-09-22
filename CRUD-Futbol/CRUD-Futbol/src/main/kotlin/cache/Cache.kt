package cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import org.example.Models.Jugador
import org.lighthousegames.logging.logging

fun darCacheJugadores(): Cache<Long, Jugador> {
    val logger = logging()
    logger.debug { "Creando cache de jugadores" }
    return Caffeine.newBuilder().maximumSize(5).build()
}