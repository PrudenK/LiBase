package com.pruden.p4_pmdm.adapters

import android.widget.TextView
import com.pruden.p4_pmdm.classes.entities.PartidaEntity

interface OnClickListenerPartidas {
    fun onClickPartida(partidaEntity: PartidaEntity)
    fun onClickFavorito(partidaEntity: PartidaEntity, fav : Boolean)
    fun onClickVisible(partidaEntity: PartidaEntity, visible: Boolean)
    fun onClickPGN(textViewMovimeintos : TextView)
    fun onClickVariante(partidaEntity: PartidaEntity)
    fun onClickBlanco(partidaEntity: PartidaEntity)
    fun onClickNegro(partidaEntity: PartidaEntity)
    fun onClickVerEnLichess(partidaEntity: PartidaEntity)
    fun onLongClickPartida(partidaEntity: PartidaEntity)
    fun onLongClickMovimientos(partidaEntity: PartidaEntity)
}