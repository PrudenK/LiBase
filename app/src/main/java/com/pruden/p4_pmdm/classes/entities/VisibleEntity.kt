package com.pruden.p4_pmdm.classes.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "VisibleEntity",
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
data class VisibleEntity(
    val idUsuario: Long,
    val idPartida: Long
)