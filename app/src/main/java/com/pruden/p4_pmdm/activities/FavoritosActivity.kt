package com.pruden.p4_pmdm.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pruden.p4_pmdm.Metodos.Filtrados.filtradoFavoritosEnUI
import com.pruden.p4_pmdm.Metodos.funcionesBottomAppBar
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.adapters.AperturasAdapter
import com.pruden.p4_pmdm.adapters.OnClickListenerAperturas
import com.pruden.p4_pmdm.classes.Apertura
import com.pruden.p4_pmdm.databinding.ActivityFavoritosBinding

class FavoritosActivity : AppCompatActivity(), OnClickListenerAperturas {
    private lateinit var favoritosBinding: ActivityFavoritosBinding

    private lateinit var linearLayoutAperturas: RecyclerView.LayoutManager
    private lateinit var aperturasAdapter: AperturasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        favoritosBinding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(favoritosBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cargarFondo()
        funcionesBottomAppBar(findViewById(R.id.bottom_app_bar), this)


        aperturasAdapter = AperturasAdapter(mutableListOf(), this, "Favoritos")


        linearLayoutAperturas = LinearLayoutManager(this)
        favoritosBinding.recyclerFavortios.apply {
            adapter = aperturasAdapter
            layoutManager = linearLayoutAperturas
        }


        filtradoFavoritosEnUI(aperturasAdapter, this)
    }

    override fun onClickApertura(apertura: Apertura) {
        val intent = Intent(this, PartidasPorAperturaActivity::class.java)
        intent.putExtra("eco", "${apertura.eco}@@Favoritos")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        filtradoFavoritosEnUI(aperturasAdapter, this)
    }

    fun cargarFondo(){
        Glide.with(favoritosBinding.root)
            .load(R.drawable.fondo)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(favoritosBinding.imgFondo)
    }
}