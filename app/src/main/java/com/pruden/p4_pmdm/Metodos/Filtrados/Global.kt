package com.pruden.p4_pmdm.Metodos.Filtrados

import android.app.Activity
import android.widget.TextView
import com.pruden.p4_pmdm.Metodos.listaAperturas
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.GlobalActivity
import com.pruden.p4_pmdm.adapters.AperturasAdapter
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.classes.Apertura

fun filtradoGlobalEnUi(aperturasAdapter: AperturasAdapter, activity: Activity) {
    filtrarGlobalAperturas(listaAperturas) { listaFiltrada ->
        activity.runOnUiThread {
            aperturasAdapter.listaAperturas.clear()
            aperturasAdapter.listaAperturas.addAll(listaFiltrada)
            aperturasAdapter.notifyDataSetChanged()

            if(listaFiltrada.isEmpty()){
                val vista = activity as GlobalActivity
                vista.findViewById<TextView>(R.id.texto_si_no_hay_aperturas).text = activity.getString(
                    R.string.no_hay_partidas)
            }
        }
    }
}

private fun filtrarGlobalAperturas(lista: List<Apertura>, callback: (List<Apertura>) -> Unit) {
    Thread {
        val listaFiltrada = lista.filter { apertura ->
            LiBaseApplication.database.partidasDao().getCountPartidasPorAperturaEcoVisibles(apertura.eco) > 0
        }
        callback(listaFiltrada)
    }.start()
}