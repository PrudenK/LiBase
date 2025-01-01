package com.pruden.p4_pmdm.baseDatos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pruden.p4_pmdm.classes.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Query("Select * from usuarioentity")
    fun getAllUsuarios(): MutableList<UsuarioEntity>

    @Query("Select id from usuarioentity where nombre = :nombreIntro")
    fun getIdUsuarioActual(nombreIntro : String):Long

    @Insert
    fun addUsuario(usuarioEntity: UsuarioEntity)


}