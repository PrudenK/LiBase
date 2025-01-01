package com.pruden.p4_pmdm.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pruden.p4_pmdm.classes.entities.FavoritosEntity
import com.pruden.p4_pmdm.classes.entities.PartidaEntity

@Dao
interface FavoritosDao {
    @Query("""
        Select * from PartidaEntity inner join FavoritosEntity on PartidaEntity.id = FavoritosEntity.idPartida where FavoritosEntity.idUsuario = :idUsuario
    """)
    fun obtenerPartidasFavoritasPorUsuario(idUsuario: Long): List<PartidaEntity>

    @Insert
    fun insertarFavorito(favorito: FavoritosEntity)

    @Delete
    fun eliminarFavorito(favorito: FavoritosEntity)

    @Query("SELECT COUNT(*) > 0 FROM FavoritosEntity WHERE idUsuario = :idUsuario AND idPartida = :idPartida")
    fun existeFavorito(idUsuario: Long, idPartida: Long): Boolean

    @Query("Select count(*) From FavoritosEntity where idUsuario = :idUsuario")
    fun getNumeroPartidasFavoritasPorUsuario(idUsuario: Long): Int

}
