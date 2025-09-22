package storage

import org.example.Errors.JugadorError
import org.example.Models.Club
import org.example.Models.Jugador
import org.example.Models.Posicion
import org.example.Storage.StorageImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class StorageImplTest{
 val storage = StorageImpl()
 private val jugador = Jugador(
  id = 1,
  dorsal = 1,
  nombre = "Pepe Viyuela",
  posicion = Posicion.CENTRO,
  club = Club.REAL_MADRID
 )
 val list = listOf(jugador)
 @Test
 fun fileRead(@TempDir tempDir: File) {
  val file = File(tempDir, "test.csv")
  file.writeText("id,dorsal,nombre,posicion,club\n1,1,Pepe Viyuela,CENTRO,REAL_MADRID")
  val lista = storage.fileRead(file).value
  val actual = lista.first()
  assertAll(
   { assertEquals(jugador.nombre, actual.nombre) },
   { assertEquals(jugador.posicion, actual.posicion) },
   { assertEquals(jugador.club, actual.club) },
   { assertEquals(jugador.dorsal, actual.dorsal) },
  )
 }
 @Test
 fun fileReadFileDoesNotExist() {
  val result = storage.fileRead(File("other.csv"))
  assertEquals(result.error.msg, "El archivo no existe o no se puede leer")
 }
 @Test
 fun fileWrite(@TempDir tempDir: File) {
  val file = File(tempDir, "test.csv")
  val result = storage.fileWrite(list, file)
  assertTrue(!result.isErr)
 }
 @Test
 fun fileWriteParentIsntDirectory() {
  val file1 = File("test1.csv")
  val file2 = File(file1, "test2.csv")
  val result = storage.fileWrite(list, file2)
  assertTrue(result.isErr)
 }
}