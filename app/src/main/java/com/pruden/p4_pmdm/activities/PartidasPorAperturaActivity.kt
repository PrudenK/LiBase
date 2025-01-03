package com.pruden.p4_pmdm.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pruden.p4_pmdm.Metodos.Mensajes.dialogBorrarPartida
import com.pruden.p4_pmdm.Metodos.Mensajes.makeToast
import com.pruden.p4_pmdm.Metodos.cargarFondo
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.adapters.OnClickListenerPartidas
import com.pruden.p4_pmdm.adapters.PartidasAdapter
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import com.pruden.p4_pmdm.databinding.ActivityPartidasPorAperturaBinding
import com.pruden.p4_pmdm.Metodos.cargarFragmentAgregarPartidaManual
import com.pruden.p4_pmdm.Metodos.funcionesBottomAppBar
import com.pruden.p4_pmdm.activities.LoginActivity.Companion.idUsuarioActual
import com.pruden.p4_pmdm.classes.entities.FavoritosEntity
import com.pruden.p4_pmdm.classes.entities.VisibleEntity

class PartidasPorAperturaActivity : AppCompatActivity(), OnClickListenerPartidas, FragmentAux {
    private lateinit var partidaBinding : ActivityPartidasPorAperturaBinding

    private lateinit var linearLayoutPartidas : RecyclerView.LayoutManager
    private lateinit var partidasAdapter: PartidasAdapter

    private var partidas: MutableList<PartidaEntity> = mutableListOf()

    private lateinit var ecoGeneral : String
    private lateinit var titulo : String

    private var esVistaFavoritos = false
    private var esVistaGlobal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        partidaBinding = ActivityPartidasPorAperturaBinding.inflate(layoutInflater)
        setContentView(partidaBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.partidas_por_apertura)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        titulo = intent.getStringExtra("eco")!!
        ecoGeneral = titulo.split("@@")[0]

        esVistaFavoritos = titulo.contains("Favoritos")
        esVistaGlobal = titulo.contains("Global")

        configurarTitulos()

        filtrarPartidasPorApertura()

        cargarFondo(partidaBinding.root, partidaBinding.imgFondo)
        funcionesBottomAppBar(findViewById(R.id.bottom_app_bar), this)
    }

    private fun configurarTitulos(){
        if (esVistaFavoritos){
            partidaBinding.tituloHome.text = "Tus favoritos ($ecoGeneral)"
        }else if(esVistaGlobal){
            partidaBinding.tituloHome.text = "Partidas globales ($ecoGeneral)"
        }else{
            partidaBinding.tituloHome.text = "Tus partidas ($ecoGeneral)"
        }
    }

    private fun filtrarPartidasPorApertura(){
        Thread{
            partidas = if (esVistaFavoritos){
                LiBaseApplication.database.partidasDao().getAllPartidasFavoritasPorEco(ecoGeneral, idUsuarioActual)
            }else if(esVistaGlobal){
                LiBaseApplication.database.partidasDao().getTodasPartidasGlobalesVisiblesPorEco(ecoGeneral)
            }else{
                LiBaseApplication.database.partidasDao().getAllPartidasPorEco(ecoGeneral, idUsuarioActual)
            }

            quitarPartidasFavoritasSiNoSonVisibles()


            runOnUiThread {
                Log.d("adfa", partidas.size.toString())
                partidasAdapter = PartidasAdapter(partidas, this)
                linearLayoutPartidas = LinearLayoutManager(this)

                partidaBinding.recyclerPartida.apply {
                    adapter = partidasAdapter
                    layoutManager = linearLayoutPartidas
                }
            }
            Log.d("adfa", partidas.size.toString())
        }.start()
        Log.d("adfa", partidas.size.toString())
    }

    private fun quitarPartidasFavoritasSiNoSonVisibles(){
        if (esVistaFavoritos) {
            val nuevasPartidas = partidas.filter { partida ->
                if (partida.idUsuario != idUsuarioActual) {
                    var esVisible = true
                    val hilo = Thread {
                        esVisible = LiBaseApplication.database.visibleDao().existeVisibleSoloPartida(partida.id)
                    }
                    hilo.start()
                    hilo.join()
                    esVisible
                } else true
            }
            partidas.clear()
            partidas.addAll(nuevasPartidas)
        }
        Log.d("adfa", partidas.size.toString())
    }

    override fun onClickPGN(textViewMovimeintos : TextView) {
        if(textViewMovimeintos.maxLines == 3){
            textViewMovimeintos.maxLines = Integer.MAX_VALUE
            textViewMovimeintos.ellipsize = null
        }else{
            textViewMovimeintos.maxLines = 3
            textViewMovimeintos.ellipsize = TextUtils.TruncateAt.END
        }
    }

    private fun cargarIntent(link: String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
        }
        startActivity(intent)
    }

    override fun onClickVariante(partidaEntity: PartidaEntity) {
        cargarIntent("https://chessopenings.com/es/eco/"+partidaEntity.eco)
    }

    override fun onClickBlanco(partidaEntity: PartidaEntity) {
        cargarIntent("https://lichess.org/@/"+partidaEntity.jugadorBlancas)
    }

    override fun onClickNegro(partidaEntity: PartidaEntity) {
        cargarIntent("https://lichess.org/@/"+partidaEntity.jugadorNegras)
    }

    override fun onClickVerEnLichess(partidaEntity: PartidaEntity) {
        if (partidaEntity.url == ""){
            makeToast("Esta partida no se jugó en lichess", this)
        }else{
            cargarIntent(partidaEntity.url)
        }
    }

    override fun onLongClickPartida(partidaEntity: PartidaEntity) {
        if (comprobarSiUsuarioActualEsPropietario(partidaEntity)) {
            dialogBorrarPartida(
                context = this,
                layoutInflater = layoutInflater,
                partidasAdapter = partidasAdapter,
                partidaEntity = partidaEntity,
                activity = this
            )
        } else {
            makeToast("No puedes eliminar una partida que no es tuya", this)
        }

    }

    override fun onLongClickMovimientos(partidaEntity: PartidaEntity) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Movimientos de Partida", partidaEntity.movimientos)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "Movimientos copiados al portapapeles", Toast.LENGTH_SHORT).show()
    }

    override fun onClickPartida(partidaEntity: PartidaEntity) {
        if (comprobarSiUsuarioActualEsPropietario(partidaEntity)){
            val args = Bundle()
            args.putLong(getString(R.string.id_partida_modificar), partidaEntity.id)

            cargarFragmentAgregarPartidaManual(this, args)
        }else{
            makeToast("No puedes modificar una partida que no es tuya", this)
        }
    }

    override fun onClickFavorito(partidaEntity: PartidaEntity, fav : Boolean) {
        Thread{
            if(fav){
                LiBaseApplication.database.favoritosDao().insertarFavorito(FavoritosEntity(idUsuarioActual,partidaEntity.id))
            }else{
                LiBaseApplication.database.favoritosDao().eliminarFavorito(FavoritosEntity(idUsuarioActual,partidaEntity.id))
            }
            runOnUiThread {
                if(esVistaFavoritos && !fav){
                    filtrarPartidasPorApertura()
                    textoSiNoQuedanPartidas()
                }
            }
        }.start()

    }

    override fun onClickVisible(partidaEntity: PartidaEntity, visible: Boolean) {
        Thread{
            if(visible){
                LiBaseApplication.database.visibleDao().insertarPartidaVisible(VisibleEntity(idUsuarioActual, partidaEntity.id))
            }else{
                LiBaseApplication.database.visibleDao().eliminarPartidaVisible(VisibleEntity(idUsuarioActual, partidaEntity.id))
            }
            runOnUiThread {
                if(esVistaGlobal && !visible){
                    filtrarPartidasPorApertura()
                    textoSiNoQuedanPartidas()
                }
            }
        }.start()
    }

    override fun updatePartidaAux(partidaEntity: PartidaEntity) {
        Thread {
            LiBaseApplication.database.partidasDao().updatePartida(partidaEntity)
            runOnUiThread {
                partidasAdapter.update(partidaEntity)
                filtrarPartidasPorApertura()
                partidasAdapter.notifyDataSetChanged()
            }
        }.start()
    }


    private fun comprobarSiUsuarioActualEsPropietario(partidaEntity: PartidaEntity): Boolean{
        var esPropietario = false
        val hilo = Thread{
            esPropietario = LiBaseApplication.database.partidasDao().getSiEsPropietarioDeLaPartida(
                idUsuarioActual, partidaEntity.id)
        }

        hilo.start()
        hilo.join()

        return esPropietario
    }

    fun textoSiNoQuedanPartidas(){
        if(partidas.size == 1){
            partidaBinding.textoSiNoHayPartidas.text = getString(R.string.no_hay_partidas)
        }
    }

    override fun addPartidaAux(partidaEntity: PartidaEntity) {}
    override fun gestionFab(mostrar: Boolean) {}
}