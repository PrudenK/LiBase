package com.pruden.p4_pmdm.Metodos

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pruden.p4_pmdm.R

fun cargarLogo(vista : View, imgView : ImageView){
    Glide.with(vista)
        .load(R.mipmap.ic_libase_logo_round)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imgView)
}

fun cargarFondo(vista : View, imgView : ImageView){
    Glide.with(vista)
        .load(R.drawable.fondo)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imgView)
}