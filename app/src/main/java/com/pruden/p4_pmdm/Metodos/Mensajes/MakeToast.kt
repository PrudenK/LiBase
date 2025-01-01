package com.pruden.p4_pmdm.Metodos.Mensajes

import android.content.Context
import android.widget.Toast

var toastActual : Toast? = null

fun makeToast(mensaje : String, context: Context){
    toastActual?.cancel()
    toastActual = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT)
    toastActual?.show()
}
