package service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.*
import org.example.Errors.JugadorError
import org.example.Models.Club
import org.example.Models.Jugador
import org.example.Models.Posicion
import org.example.Repository.JugadorRepositoryImpl
import org.example.Service.ServiceImpl
import org.example.Storage.StorageImpl
import org.example.Validator.Validator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.io.File
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class ServiceImplTest {

 @Mock private lateinit var repo: JugadorRepositoryImpl
 @Mock private lateinit var cache: Cache<Long, Jugador>
 @Mock private lateinit var validator: Validator
 @Mock private lateinit var storage: StorageImpl

 private lateinit var service: ServiceImpl

 private val jugador = Jugador(
  id = 1,
  dorsal = 10,
  nombre = "Raúl González",
  posicion = Posicion.DELANTERO,
  club = Club.REAL_MADRID
 )
 private val jugadores = listOf(jugador)

 @BeforeEach
 fun setUp() {
  service = ServiceImpl(repo, cache, validator, storage)
 }

 @Test
 fun getAll() {
  whenever(repo.getAll()) doReturn jugadores

  val result = service.getAll()

  assertTrue(result.isOk)
  verify(repo).getAll()
 }

 @Test
 fun getAllError() {
  whenever(repo.getAll()).thenThrow(RuntimeException("DB error"))

  val result = service.getAll()

  assertTrue(result.isErr)
  verify(repo).getAll()
 }

 @Test
 fun getByIdCache() {
  whenever(cache.getIfPresent(1)) doReturn jugador

  val result = service.getById(1)

  assertTrue(result.isOk)
  verify(cache).getIfPresent(1)
  verifyNoInteractions(repo)
 }

 @Test
 fun getByIdRepo() {
  whenever(cache.getIfPresent(1)) doReturn null
  whenever(repo.getById(1)) doReturn jugador

  val result = service.getById(1)

  assertTrue(result.isOk)
  verify(repo).getById(1)
  verify(cache).put(1, jugador)
 }

 @Test
 fun getByIdError() {
  whenever(cache.getIfPresent(1)) doReturn null
  whenever(repo.getById(1)) doReturn null

  val result = service.getById(1)

  assertTrue(result.isErr)
 }

 @Test
 fun fileWrite() {
  val file = File("jugadores.csv")
  whenever(repo.getAll()) doReturn jugadores
  whenever(storage.fileWrite(jugadores, file)) doReturn Ok(Unit)

  val result = service.fileWrite(file)

  assertTrue(result.isOk)
  verify(storage).fileWrite(jugadores, file)
 }

 @Test
 fun fileWriteError() {
  val file = File("jugadores.csv")
  whenever(repo.getAll()) doReturn jugadores
  whenever(storage.fileWrite(any(), eq(file))) doReturn Err(JugadorError.DatabaseError("fallo"))

  val result = service.fileWrite(file)

  assertTrue(result.isErr)
 }

 @Test
 fun fileRead() {
  val file = File("jugadores.csv")
  whenever(storage.fileRead(file)) doReturn Ok(jugadores)
  whenever(validator.validate(jugador)) doReturn Ok(jugador)
  whenever(repo.save(jugador)) doReturn jugador

  val result = service.fileRead(file)

  assertTrue(result.isOk)
  verify(repo).save(jugador)
 }

 @Test
 fun fileReadError() {
  val file = File("jugadores.csv")
  whenever(storage.fileRead(file)) doReturn Ok(jugadores)
  whenever(validator.validate(jugador)) doReturn Err(JugadorError.InvalidoError("nombre inválido"))

  val result = service.fileRead(file)

  assertTrue(result.isErr)
  verify(repo, never()).save(any())
 }

 @Test
 fun delete() {
  whenever(repo.delete(1)) doReturn jugador

  val result = service.delete(1)

  assertTrue(result.isOk)
  verify(cache).invalidate(1)
 }

 @Test
 fun deleteError() {
  whenever(repo.delete(1)) doReturn null

  val result = service.delete(1)

  assertTrue(result.isErr)
  verify(cache, never()).invalidate(any())
 }

 @Test
 fun update() {
  whenever(validator.validate(jugador)) doReturn Ok(jugador)
  whenever(repo.update(1, jugador)) doReturn jugador
  whenever(cache.getIfPresent(1)) doReturn jugador

  val result = service.update(1, jugador)

  assertTrue(result.isOk)
  verify(cache).invalidate(1)
  verify(cache).put(1, jugador)
 }

 @Test
 fun updateError() {
  whenever(validator.validate(jugador)) doReturn Ok(jugador)
  whenever(repo.update(1, jugador)) doReturn null

  val result = service.update(1, jugador)

  assertTrue(result.isErr)
 }

 @Test
 fun save() {
  whenever(validator.validate(jugador)) doReturn Ok(jugador)
  whenever(repo.save(jugador)) doReturn jugador

  val result = service.save(jugador)

  assertTrue(result.isOk)
  verify(repo).save(jugador)
 }

 @Test
 fun saveInvalid() {
  whenever(validator.validate(jugador)) doReturn Err(JugadorError.InvalidoError("dorsal inválido"))

  val result = service.save(jugador)

  assertTrue(result.isErr)
  verify(repo, never()).save(any())
 }
 @Test
 fun getByIdDbException() {
  whenever(cache.getIfPresent(1)) doReturn null
  whenever(repo.getById(1)).thenThrow(RuntimeException("DB error"))

  val result = service.getById(1)

  assertTrue(result.isErr)
 }

 @Test
 fun fileWriteException() {
  val file = File("jugadores.csv")
  whenever(repo.getAll()).thenThrow(RuntimeException("DB error"))

  val result = service.fileWrite(file)

  assertTrue(result.isErr)
  verify(storage, never()).fileWrite(any(), any())
 }

 @Test
 fun fileReadException() {
  val file = File("jugadores.csv")
  whenever(storage.fileRead(file)).thenThrow(RuntimeException("IO error"))

  val result = service.fileRead(file)

  assertTrue(result.isErr)
 }

 @Test
 fun updateException() {
  whenever(validator.validate(jugador)) doReturn Ok(jugador)
  whenever(repo.update(1, jugador)).thenThrow(RuntimeException("DB error"))

  val result = service.update(1, jugador)

  assertTrue(result.isErr)
 }

 @Test
 fun saveException() {
  whenever(validator.validate(jugador)) doReturn Ok(jugador)
  whenever(repo.save(jugador)).thenThrow(RuntimeException("DB error"))

  val result = service.save(jugador)

  assertTrue(result.isErr)
 }
}
