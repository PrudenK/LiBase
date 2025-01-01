package com.pruden.p4_pmdm.Metodos.Mensajes

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pruden.p4_pmdm.Metodos.listaLinks
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.adapters.LinksAdapter
import com.pruden.p4_pmdm.classes.entities.PartidaEntity

fun dialogoSubirDatos(
    context: Context,
    layoutInflater: LayoutInflater,
    onProcesarPartida: (String, (PartidaEntity?) -> Unit) -> Unit,
    onGuardarPartida: (PartidaEntity, () -> Unit) -> Unit,
    onFiltradoEnUI: () -> Unit
) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_agregar_datos_db, null)

    val dialog = MaterialAlertDialogBuilder(context)
        .setView(dialogView)
        .create()

    dialog.show()

    dialog.window?.let { window ->
        val params = window.attributes
        params.gravity = Gravity.TOP
        params.y = 100
        window.attributes = params
    }

    val btnClose = dialogView.findViewById<Button>(R.id.btn_close)
    btnClose.setOnClickListener {
        dialog.dismiss()
    }

    val recycler = dialogView.findViewById<RecyclerView>(R.id.lista_links)
    recycler.apply {
        adapter = LinksAdapter(listaLinks)
        layoutManager = LinearLayoutManager(context)
    }

    val btnSubir = dialogView.findViewById<Button>(R.id.btn_subir)

    btnSubir.setOnClickListener {
        for(link in listaLinks){
            onProcesarPartida(link.link){ partidaProcesada ->
                if (partidaProcesada != null) {
                    onGuardarPartida(partidaProcesada) {
                        onFiltradoEnUI()
                    }
                }
            }
        }

        makeToast("¡Partidas subidas!", context)
        dialog.dismiss()
    }
}