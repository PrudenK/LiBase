package com.pruden.p4_pmdm.activities

import com.pruden.p4_pmdm.classes.entities.PartidaEntity

interface FragmentAux {
    fun addPartidaAux(partidaEntity: PartidaEntity)
    fun updatePartidaAux(partidaEntity: PartidaEntity)
    fun gestionFab(mostrar: Boolean)
}