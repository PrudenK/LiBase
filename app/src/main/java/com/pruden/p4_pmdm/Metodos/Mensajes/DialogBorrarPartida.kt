package com.pruden.p4_pmdm.Metodos.Mensajes

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.PartidasPorAperturaActivity
import com.pruden.p4_pmdm.adapters.PartidasAdapter
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.classes.entities.PartidaEntity

fun dialogBorrarPartida(
    context: Context,
    layoutInflater: LayoutInflater,
    partidasAdapter: PartidasAdapter,
    partidaEntity: PartidaEntity,
    activity: Activity
){
    val dialogView = layoutInflater.inflate(R.layout.dialog_borrar_partida, null)

    val dialog = MaterialAlertDialogBuilder(context)
        .setView(dialogView)
        .create()

    dialog.show()

    dialog.window?.let { window ->
        val params = window.attributes
        params.gravity = Gravity.TOP
        params.y = 300
        window.attributes = params
    }

    val btnCancelar = dialogView.findViewById<Button>(R.id.btn_cancelar)
    btnCancelar.setOnClickListener {
        dialog.dismiss()
    }

    val btnBorrar = dialogView.findViewById<Button>(R.id.btn_borrar)

    btnBorrar.setOnClickListener {
        val hilo = Thread {
            LiBaseApplication.database.partidasDao().deletePartida(partidaEntity)
            activity.runOnUiThread {
                partidasAdapter.delete(partidaEntity)
            }
        }
        hilo.start()
        hilo.join()

        val activityPartidas =  activity as PartidasPorAperturaActivity
        activityPartidas.textoSiNoQuedanPartidas()

        dialog.dismiss()
    }
}