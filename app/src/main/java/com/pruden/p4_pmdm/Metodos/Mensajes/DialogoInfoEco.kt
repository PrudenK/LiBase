package com.pruden.p4_pmdm.Metodos.Mensajes

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pruden.p4_pmdm.R

fun dialogoInfoEco(context: Context, layoutInflater: LayoutInflater){
    val dialogView = layoutInflater.inflate(R.layout.dialog_info_eco, null)

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
}