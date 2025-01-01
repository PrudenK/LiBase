package com.pruden.p4_pmdm.Metodos.Mensajes

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.classes.entities.PartidaEntity

fun dialogoFABURL(
    context: Context,
    layoutInflater: LayoutInflater,
    onProcesarPartida: (String, (PartidaEntity?) -> Unit) -> Unit,
    onGuardarPartida: (PartidaEntity, () -> Unit) -> Unit,
    onFiltradoEnUI: () -> Unit
) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_partida_url, null)
    val dialog = MaterialAlertDialogBuilder(context)
        .setView(dialogView)
        .create()

    val inputUrl = dialogView.findViewById<EditText>(R.id.input_url)
    val tilInputUrl = dialogView.findViewById<TextInputLayout>(R.id.til_input_url)
    val btnCancelar = dialogView.findViewById<View>(R.id.btn_cancelar)
    val btnAceptar = dialogView.findViewById<View>(R.id.btn_aceptar)

    inputUrl.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGrayColor)))
    tilInputUrl.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lightGrayColor))

    btnAceptar.setOnClickListener {
        val url = inputUrl.text.toString()
        if (url.isNotEmpty()) {
            onProcesarPartida(url) { partidaProcesada ->
                if (partidaProcesada != null) {
                    onGuardarPartida(partidaProcesada) {
                        onFiltradoEnUI()
                    }
                }
            }
            dialog.dismiss()
        }else{
            makeToast("No puedes poner una Url vacía", context)
        }
    }

    btnCancelar.setOnClickListener {
        dialog.dismiss()
    }

    dialog.show()
}