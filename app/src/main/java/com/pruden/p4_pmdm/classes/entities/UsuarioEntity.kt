package com.pruden.p4_pmdm.classes.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UsuarioEntity")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val nombre: String,
    val contra: String
)