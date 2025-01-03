package com.pruden.p4_pmdm.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pruden.p4_pmdm.classes.entities.PartidaEntity

@Dao
interface PartidasDao {
    @Query("Select * From partidaentity where idUsuario = :idUser")
    fun getAllPartidasUsuario(idUser : Long) : MutableList<PartidaEntity>

    @Query("Select count(*) From partidaentity where idUsuario = :idUser")
    fun getCountPartidas(idUser : Long) : Int

    @Query("""
    SELECT COUNT(*)
    FROM FavoritosEntity f
    INNER JOIN PartidaEntity p ON f.idPartida = p.id
    WHERE 
        p.ecoGeneral = :ecoGeneral
        AND (
            p.idUsuario = :idUser
            OR (
                p.idUsuario != :idUser
                AND EXISTS (
                    SELECT 1
                    FROM VisibleEntity v
                    WHERE v.idPartida = p.id
                )
            )
        ) and f.idUsuario = :idUser
""")
    fun gentNumeroPartidasFavoritasPorEco(ecoGeneral: String, idUser: Long): Int

    @Query("Select count(*) From partidaentity where ecoGeneral = :ecoGeneral and idUsuario = :idUser")
    fun getCountPartidasPorEco(ecoGeneral : String, idUser : Long) : Int

    @Query("""
    Select exists (
        Select 1
        From FavoritosEntity f
        inner join PartidaEntity p on f.idPartida = p.id
        where 
            p.ecoGeneral = :ecoGeneral
            and (
                (p.idUsuario = :idUser)
                or (
                    p.idUsuario != :idUser
                    and exists (
                        Select 1
                        From VisibleEntity v
                        where v.idPartida = p.id
                    )
                )
            )
        and f.idUsuario = :idUser    
    )
""")
    fun getSiAperturasTienePartidasPorEcoFavoritasYSonVisibles(ecoGeneral : String, idUser : Long) : Boolean

    @Query("Select count(*) From visibleentity v inner join partidaentity p on v.idPartida = p.id where ecoGeneral = :ecoGeneral")
    fun getCountPartidasPorAperturaEcoVisibles(ecoGeneral: String): Int

    @Query("Select count(*) From visibleentity v inner join partidaentity p on v.idPartida = p.id")
    fun getCountPartidasVisibles(): Int

    @Query("Select * From visibleentity v inner join partidaentity p on v.idPartida = p.id where ecoGeneral = :ecoGeneral")
    fun getTodasPartidasGlobalesVisiblesPorEco(ecoGeneral: String): MutableList<PartidaEntity>

    @Query("Select * From partidaentity where ecoGeneral = :ecoGeneral and idUsuario = :idUser")
    fun getAllPartidasPorEco(ecoGeneral: String, idUser : Long) : MutableList<PartidaEntity>

    @Query("""Select * From favoritosentity f inner join partidaentity p on f.idPartida = p.id 
        where f.idUsuario = :idUser and p.ecoGeneral = :ecoGeneral""")
    fun getAllPartidasFavoritasPorEco(ecoGeneral: String, idUser: Long): MutableList<PartidaEntity>

    @Query("Select * From partidaentity where id = :id")
    fun getPartidaPorId(id : Long): PartidaEntity

    @Update
    fun updatePartida(partidaEntity: PartidaEntity)

    @Insert
    fun addPartida(partidaEntity: PartidaEntity): Long

    @Delete
    fun deletePartida(partidaEntity: PartidaEntity)

    @Query("Select count(*) From partidaentity where idUsuario = :idUser and id = :idPartida")
    fun getSiEsPropietarioDeLaPartida(idUser: Long, idPartida : Long): Boolean
}