package com.pruden.p4_pmdm.classes.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "FavoritosEntity",
    primaryKeys = ["idUsuario", "idPartida"],
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PartidaEntity::class,
            parentColumns = ["id"],
            childColumns = ["idPartida"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoritosEntity(
    val idUsuario: Long,
    val idPartida: Long
)