package com.pruden.p4_pmdm.Metodos

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.FavoritosActivity
import com.pruden.p4_pmdm.activities.GlobalActivity
import com.pruden.p4_pmdm.activities.HomeActivity
import com.pruden.p4_pmdm.activities.LoginActivity


fun funcionesBottomAppBar(vista : View, activity: Activity){

    val volverAtras = vista.findViewById<View>(R.id.volver_atras)
    val homeLogin = vista.findViewById<View>(R.id.home_login)
    val biblioteca = vista.findViewById<ImageView>(R.id.biblioteca)
    val global = vista.findViewById<ImageView>(R.id.global)
    val favoritos = vista.findViewById<ImageView>(R.id.favortios)

    volverAtras.setOnClickListener {
        activity.finish()
    }

    if (activity::class.java == FavoritosActivity::class.java) {
        favoritos.setColorFilter(ContextCompat.getColor(activity, R.color.lightGrayColor), PorterDuff.Mode.SRC_IN)
    } else if (activity::class.java == HomeActivity::class.java) {
        biblioteca.setColorFilter(ContextCompat.getColor(activity, R.color.lightGrayColor), PorterDuff.Mode.SRC_IN)
    }else if (activity::class.java == GlobalActivity::class.java) {
        global.setColorFilter(ContextCompat.getColor(activity, R.color.lightGrayColor), PorterDuff.Mode.SRC_IN)
    }

    abrirVista(activity, LoginActivity::class.java, homeLogin)
    abrirVista(activity, HomeActivity::class.java, biblioteca)
    abrirVista(activity, GlobalActivity::class.java, global)
    abrirVista(activity, FavoritosActivity::class.java, favoritos)
}

private fun abrirVista(activity: Activity, vista: Class<out Activity>, icono : View) {
    icono.setOnClickListener {
        if (activity::class.java != vista) {
            val intent = Intent(activity, vista)
            if(icono.id == R.id.home_login){
                intent.putExtra("ir_a_login", true)
            }
            activity.startActivity(intent)
        }
    }
}