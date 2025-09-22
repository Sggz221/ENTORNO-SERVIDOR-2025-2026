package repository

import dao.JugadorDao
import org.example.DAO.JugadorEntity
import org.example.Models.Club
import org.example.Models.Jugador
import org.example.Models.Posicion
import org.example.Repository.JugadorRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class JugadorRepositoryImplTest{
    @Mock
    private lateinit var dao: JugadorDao

    @InjectMocks
    private lateinit var repository: JugadorRepositoryImpl

    private val jugador = Jugador(
        id = 1,
        dorsal = 1,
        nombre = "Pepe Viyuela",
        posicion = Posicion.CENTRO,
        club = Club.REAL_MADRID
    )
    private val jugadorEntity = JugadorEntity(
        id = 1,
        dorsal = 1,
        nombre = "Pepe Viyuela",
        posicion = "CENTRO",
        club = "REAL_MADRID",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun getAll(){
        whenever(dao.getAll()) doReturn listOf(jugadorEntity)

        val list = repository.getAll()
        assertEquals(1, list.size)
        assertNotNull(list)
        assertTrue(list.isNotEmpty())

        verify(dao, times(1)).getAll()
    }

    @Test
    fun getById(){
        whenever(dao.getById(1))doReturn jugadorEntity

        val result = repository.getById(1)
        assertAll(
            { assertEquals(jugador.nombre, result!!.nombre) },
            { assertEquals(jugador.posicion, result!!.posicion) },
            { assertEquals(jugador.club, result!!.club) },
            { assertEquals(jugador.dorsal, result!!.dorsal) },
        )
        verify(dao, times(1)).getById(1)
    }

    @Test
    fun getByIdNull(){
        whenever(dao.getById(1)).thenReturn(null)
        val result = repository.getById(1)
        assertNull(result)
        verify(dao, times(1)).getById(1)
    }

    @Test
    fun save(){
        whenever(dao.save(jugadorEntity)) doReturn 1
        val saved = repository.save(jugador)
        assertAll(
            { assertEquals(jugador.nombre, saved!!.nombre) },
            { assertEquals(jugador.posicion, saved!!.posicion) },
            { assertEquals(jugador.club, saved!!.club) },
            { assertEquals(jugador.dorsal, saved!!.dorsal) },
        )
        verify(dao, times(1)).save(jugadorEntity)
    }

    @Test
    fun delete(){
        whenever(dao.delete(jugadorEntity.id)) doReturn 1
        whenever(dao.getById(1)).thenReturn(jugadorEntity)

        val result = repository.delete(jugador.id)

        assertEquals(result!!.nombre, jugador.nombre)
        assertNotNull(result)
        assertEquals(result.id, jugador.id)

        verify(dao, times(1)).delete(jugadorEntity.id)
        verify(dao, times(1)).getById(jugadorEntity.id)
    }

    @Test
    fun deleteNull(){
        whenever(dao.getById(1)).thenReturn(null)
        val result = repository.delete(1)
        assertNull(result)
        verify(dao, times(1)).getById(1)
    }

    @Test
    fun update(){
        whenever(dao.getById(1)) doReturn jugadorEntity

        val result = repository.update(1, jugador)
        assertAll(
            { assertEquals(jugador.nombre, result!!.nombre) },
            { assertEquals(jugador.posicion, result!!.posicion) },
            { assertEquals(jugador.club, result!!.club) },
            { assertEquals(jugador.dorsal, result!!.dorsal) },
        )
        verify(dao, times(1)).getById(1)
    }

    @Test
    fun updateNull(){
        whenever(dao.getById(1)).thenReturn(null)
        val result = repository.update(1, jugador)
        assertNull(result)
        verify(dao, times(1)).getById(1)
    }
}