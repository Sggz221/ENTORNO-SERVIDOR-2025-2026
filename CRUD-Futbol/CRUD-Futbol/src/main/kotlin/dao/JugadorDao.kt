package dao
import org.example.DAO.JugadorEntity
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.lighthousegames.logging.logging

@RegisterKotlinMapper(JugadorEntity::class)
interface JugadorDao {
    @SqlQuery("SELECT * FROM jugadores")
    fun getAll(): List<JugadorEntity>

    @SqlQuery("SELECT * FROM jugadores WHERE id = :id")
    fun getById(@Bind("id") id: Long): JugadorEntity?

    @SqlUpdate("INSERT INTO jugadores(dorsal, nombre, posicion, club, created_at, updated_at) VALUES (:dorsal, :nombre, :posicion, :club, :createdAt, :updatedAt)")
    @GetGeneratedKeys("id")
    fun save(@BindBean jugador: JugadorEntity): Int

    @SqlUpdate("UPDATE jugadores SET dorsal = :dorsal, nombre = :nombre, posicion = :posicion, club = :club, created_at = :createdAt, updated_at = :updatedAt WHERE id = :identification")
    fun update(@BindBean jugador: JugadorEntity, @Bind("identification") identification: Long): Int

    @SqlUpdate("DELETE FROM jugadores WHERE id = :id")
    fun delete(@Bind("id") id: Long): Int

    @SqlUpdate("DELETE FROM jugadores")
    fun deleteAll(): Int
}

fun darPersonasDao(jdbi: Jdbi): JugadorDao {
    val logger = logging()
    logger.debug { "Dando DAO de Jugadores" }
    return jdbi.onDemand(JugadorDao::class.java)
}