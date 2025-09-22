package dao

import org.example.DAO.JugadorEntity
import org.example.database.JdbiManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JugadorDaoTest{
    val jugadorEntity = JugadorEntity(
        id = 1,
        dorsal = 1,
        nombre = "Pepe Viyuela",
        posicion = "CENTRO",
        club = "REAL_MADRID",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
    private lateinit var dao: JugadorDao
    @BeforeAll
    fun setUp() {
        val jdbi = JdbiManager(true).jdbi
        dao = darPersonasDao(jdbi)
    }

    @AfterEach
    fun tearDown(){
        dao.deleteAll()
    }

    @Test
    fun save(){
        val id = dao.save(jugadorEntity)
        assertNotNull(id, "No deberia ser nulo")
    }
    @Test
    fun delete(){
        val id = dao.save(jugadorEntity)
        val deleted = dao.delete(id.toLong())
        val result = dao.getById(id.toLong())

        assertNull(result, "No deberia existir")
        assertEquals(deleted, 1, "La base de datos deberia devolver 1 fila afectada")
    }
    @Test
    fun update(){
        val id = dao.save(jugadorEntity)
        val normal = dao.getById(id.toLong())
        dao.update(jugadorEntity.copy(updatedAt = LocalDateTime.now()), id.toLong())
        val actualizado = dao.getById(id.toLong())
        assertNotEquals(normal!!.updatedAt, actualizado!!.updatedAt)
    }
    @Test
    fun getAll() {
        val id = dao.save(jugadorEntity)
        val list = dao.getAll()
        val saved = dao.getById(id.toLong())
        assertEquals(list.size, 1, "Deberia tener size 1")
        assertAll(
            { assertEquals(list[0].dorsal, jugadorEntity.dorsal) },
            { assertEquals(list[0].nombre, jugadorEntity.nombre) },
            { assertEquals(list[0].posicion, jugadorEntity.posicion) },
            { assertEquals(list[0].club, jugadorEntity.club) }
        )
    }
    @Test
    fun getById() {
        val id = dao.save(jugadorEntity)
        val jugadorPorId = dao.getById(id.toLong())
        assertAll(
            { assertEquals(jugadorPorId!!.dorsal, jugadorEntity.dorsal) },
            { assertEquals(jugadorPorId!!.nombre, jugadorEntity.nombre) },
            { assertEquals(jugadorPorId!!.posicion, jugadorEntity.posicion) },
            { assertEquals(jugadorPorId!!.club, jugadorEntity.club) }
        )
    }
}