package com.pruden.p4_pmdm.classes.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PartidaEntity")
data class PartidaEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var idUsuario: Long,
    var eco : String,
    var ecoGeneral :String,
    var movimientos : String,
    var tiempo: String,
    var fecha: String,
    var variante: String,
    var jugadorBlancas: String,
    var eloBlancas: String,
    var jugadorNegras: String,
    var eloNegras: String,
    var resultado: String,
    var url : String
)
