package com.pruden.p4_pmdm.Metodos.Filtrados

import android.app.Activity
import android.widget.TextView

import com.pruden.p4_pmdm.Metodos.listaAperturas
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.HomeActivity
import com.pruden.p4_pmdm.activities.LoginActivity.Companion.idUsuarioActual
import com.pruden.p4_pmdm.adapters.AperturasAdapter
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.classes.Apertura

fun filtradoEnUI(aperturasAdapter: AperturasAdapter, activity: Activity) {
    filtrarAperturas(listaAperturas) { listaFiltrada ->
        activity.runOnUiThread {
            aperturasAdapter.listaAperturas.clear()
            aperturasAdapter.listaAperturas.addAll(listaFiltrada)
            aperturasAdapter.notifyDataSetChanged()

            val vista = activity as HomeActivity
            if(listaFiltrada.isEmpty()){
                vista.findViewById<TextView>(R.id.texto_si_no_hay_aperturas).text = activity.getString(
                    R.string.no_hay_partidas)
            }else{
                vista.findViewById<TextView>(R.id.texto_si_no_hay_aperturas).text = ""
            }
        }
    }
}

private fun filtrarAperturas(lista: List<Apertura>, callback: (List<Apertura>) -> Unit) {
    Thread {
        val listaFiltrada = lista.filter { apertura ->
            LiBaseApplication.database.partidasDao().getCountPartidasPorEco(apertura.eco, idUsuarioActual) > 0
        }
        callback(listaFiltrada)
    }.start()
}