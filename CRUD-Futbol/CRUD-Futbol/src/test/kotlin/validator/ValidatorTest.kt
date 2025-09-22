package validator

import org.example.Models.Club
import org.example.Models.Jugador
import org.example.Models.Posicion
import org.example.Validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class ValidatorTest{
 val validator = Validator()
 val jugadorBien = Jugador(
  id = 1,
  dorsal = 1,
  nombre = "Pepe Viyuela",
  posicion = Posicion.CENTRO,
  club = Club.REAL_MADRID,
  createdAt = LocalDateTime.now(),
  updatedAt = LocalDateTime.now()
 )

  @Test
  fun validarBien(){
   val validado = validator.validate(jugadorBien)
   assertEquals(true, validado.isOk)
  }
 @Test
 fun validarNombreMal(){
  val validado = validator.validate(jugadorBien.copy(nombre = ""))
  assertEquals(true, validado.isErr)
 }
 @Test
 fun validarNombreMal2(){
  val validado = validator.validate(jugadorBien.copy(nombre = "12345 12345 12345 123345 12345 12345 12345 12345 12345 12345 12345"))
  assertEquals(true, validado.isErr)
 }
 @Test
 fun validarDorsalMal(){
  val validado = validator.validate(jugadorBien.copy(dorsal = 0))
  assertEquals(true, validado.isErr)
 }
 @Test
 fun validarDorsalMal2(){
  val validado = validator.validate(jugadorBien.copy(dorsal = 100))
  assertEquals(true, validado.isErr)
 }
}