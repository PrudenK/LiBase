package com.pruden.p4_pmdm.Metodos

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.pruden.p4_pmdm.fragments.AgregarPartidaManualFragment
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.HomeActivity

fun cargarFragmentAgregarPartidaManual(activity: FragmentActivity,  argumentos : Bundle? = null){
    val fragment = AgregarPartidaManualFragment()

    val fragmentManager = activity.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    if(argumentos != null){
        fragment.arguments = argumentos
        fragmentTransaction.add(R.id.partidas_por_apertura, fragment)
    }else{
        fragmentTransaction.add(R.id.home, fragment)
    }

    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()


    if (activity is HomeActivity) {
        activity.gestionFab(false)
    }
}

