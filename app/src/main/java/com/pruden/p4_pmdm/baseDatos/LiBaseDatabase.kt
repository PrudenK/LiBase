package com.pruden.p4_pmdm.baseDatos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pruden.p4_pmdm.baseDatos.DAO.FavoritosDao
import com.pruden.p4_pmdm.baseDatos.DAO.PartidasDao
import com.pruden.p4_pmdm.baseDatos.DAO.UsuarioDao
import com.pruden.p4_pmdm.baseDatos.DAO.VisibleDao
import com.pruden.p4_pmdm.classes.entities.FavoritosEntity
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import com.pruden.p4_pmdm.classes.entities.UsuarioEntity
import com.pruden.p4_pmdm.classes.entities.VisibleEntity

@Database(entities =  [PartidaEntity::class, UsuarioEntity::class, FavoritosEntity::class, VisibleEntity::class], version = 3)
abstract class LiBaseDatabase : RoomDatabase(){
    abstract fun partidasDao(): PartidasDao
    abstract fun usuariosDao(): UsuarioDao
    abstract fun favoritosDao(): FavoritosDao
    abstract fun visibleDao(): VisibleDao
}