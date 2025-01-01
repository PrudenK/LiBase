package com.pruden.p4_pmdm.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pruden.p4_pmdm.Metodos.Filtrados.filtradoEnUI
import com.pruden.p4_pmdm.Metodos.Mensajes.dialogoFABURL
import com.pruden.p4_pmdm.Metodos.Mensajes.dialogoInfoEco
import com.pruden.p4_pmdm.Metodos.Mensajes.dialogoSubirDatos
import com.pruden.p4_pmdm.Metodos.Mensajes.makeToast
import com.pruden.p4_pmdm.Metodos.procesarPartida
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.Metodos.cargarFragmentAgregarPartidaManual
import com.pruden.p4_pmdm.Metodos.funcionesBottomAppBar
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.adapters.AperturasAdapter
import com.pruden.p4_pmdm.adapters.OnClickListenerAperturas
import com.pruden.p4_pmdm.classes.Apertura
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import com.pruden.p4_pmdm.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), OnClickListenerAperturas, FragmentAux {
    private lateinit var homeBinding: ActivityHomeBinding

    private lateinit var linearLayoutAperturas: RecyclerView.LayoutManager
    private lateinit var aperturasAdapter: AperturasAdapter

    private var fabExpandido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cargarFondo()
        funcionesBottomAppBar(findViewById(R.id.bottom_app_bar), this)
        cargarColorDeLosFabs(homeBinding.fabMenu, homeBinding.fabUrl, homeBinding.fabManual)

        aperturasAdapter = AperturasAdapter(mutableListOf(), this, "Home")

        linearLayoutAperturas = LinearLayoutManager(this)
        homeBinding.recyclerHome.apply {
            adapter = aperturasAdapter
            layoutManager = linearLayoutAperturas
        }

        filtradoEnUI(aperturasAdapter, this)


        fabUrl()
        fabMenu()
        fabManual()


        menuExtra()
    }

    fun guardarPartida(partida: PartidaEntity, onComplete: () -> Unit) {
        Thread {
            LiBaseApplication.database.partidasDao().addPartida(partida)
            runOnUiThread {
                onComplete()
            }
        }.start()
    }

    override fun onClickApertura(apertura: Apertura) {
        val intent = Intent(this, PartidasPorAperturaActivity::class.java)
        intent.putExtra("eco", apertura.eco)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        filtradoEnUI(aperturasAdapter, this)
    }

    override fun addPartidaAux(partidaEntity: PartidaEntity) {
        guardarPartida(partidaEntity){
            filtradoEnUI(aperturasAdapter, this)
        }
    }

    override fun gestionFab(mostrar: Boolean) {
        with(homeBinding){
            if(mostrar){
                fabMenu.visibility = View.VISIBLE
                fabExpandido = !fabExpandido
            }else{
                fabMenu.visibility = View.GONE
                fabManual.visibility = View.GONE
                fabUrl.visibility = View.GONE
            }
        }
    }

    private fun fabUrl(){
        homeBinding.fabUrl.setOnClickListener {
            dialogoFABURL(
                context = this,
                layoutInflater = layoutInflater,
                onProcesarPartida = { url, callback -> procesarPartida(url, callback, this, true) },
                onGuardarPartida = { partida, callback -> guardarPartida(partida, callback) },
                onFiltradoEnUI = { filtradoEnUI(aperturasAdapter, this) }
            )
        }
    }

    private fun fabMenu(){
        with(homeBinding){
            fabMenu.setOnClickListener {
                if (fabExpandido) {
                    fabManual.visibility = View.GONE
                    fabUrl.visibility = View.GONE
                } else {
                    fabManual.visibility = View.VISIBLE
                    fabUrl.visibility = View.VISIBLE
                }
                fabExpandido = !fabExpandido
            }
        }
    }

    private fun fabManual(){
        homeBinding.fabManual.setOnClickListener {
            cargarFragmentAgregarPartidaManual(this)
        }
    }

    private fun menuExtra(){
        homeBinding.menuExtra.setOnClickListener {
            val contextWrapper = ContextThemeWrapper(this, R.style.CustomPopupMenu)

            val popupMenu = PopupMenu(contextWrapper, homeBinding.menuExtra)
            popupMenu.menuInflater.inflate(R.menu.menu_extra, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_option_1 -> {
                        dialogoInfoEco(this, layoutInflater)
                        true
                    }
                    R.id.menu_option_2 ->{
                        dialogoSubirDatos(
                            context = this,
                            layoutInflater = layoutInflater,
                            onProcesarPartida = { url, callback -> procesarPartida(url, callback, this) },
                            onGuardarPartida = { partida, callback -> guardarPartida(partida, callback) },
                            onFiltradoEnUI = { filtradoEnUI(aperturasAdapter, this) }

                        )
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    fun cargarFondo(){
        Glide.with(homeBinding.root)
            .load(R.drawable.fondo)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(homeBinding.imgFondo)
    }

    private fun cargarColorDeLosFabs(vararg fabs : FloatingActionButton){
        for (fab in fabs){
            fab.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.lightGrayColor))
        }
    }

    override fun updatePartidaAux(partidaEntity: PartidaEntity) {}
}