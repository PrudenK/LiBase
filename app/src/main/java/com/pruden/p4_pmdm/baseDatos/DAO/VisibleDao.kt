package com.pruden.p4_pmdm.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import com.pruden.p4_pmdm.classes.entities.VisibleEntity

@Dao
interface VisibleDao {
    @Query("SELECT * FROM PartidaEntity p INNER JOIN VisibleEntity v ON p.id = v.idPartida")
    fun obtenerTodasLasPartidasVisibles(): List<PartidaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarPartidaVisible(visibleEntity: VisibleEntity)

    @Delete
    fun eliminarPartidaVisible(visibleEntity: VisibleEntity)

    @Query("SELECT COUNT(*) > 0 FROM VisibleEntity WHERE idUsuario = :idUsuario AND idPartida = :idPartida")
    fun existeVisible(idUsuario: Long, idPartida: Long): Boolean
}