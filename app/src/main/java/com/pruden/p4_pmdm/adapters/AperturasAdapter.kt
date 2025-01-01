package com.pruden.p4_pmdm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pruden.p4_pmdm.Metodos.descripcionPorEco
import com.pruden.p4_pmdm.Metodos.imagenPorEco
import com.pruden.p4_pmdm.Metodos.nombrePorEco
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.LoginActivity
import com.pruden.p4_pmdm.classes.Apertura
import com.pruden.p4_pmdm.databinding.ItemAperturaBinding

class AperturasAdapter(val listaAperturas : MutableList<Apertura>,
                       val listenerAperturas: OnClickListenerAperturas,
                       val modo : String)
    : RecyclerView.Adapter<AperturasAdapter.ViewHolder>()
{

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemAperturaBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_apertura, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int = listaAperturas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apertura = listaAperturas[position]

        with(holder) {
            binding.codigoEco.text = apertura.eco
            binding.nombreApertura.text = nombrePorEco(apertura.eco)
            binding.descripcion.text = descripcionPorEco(apertura.eco)

            Glide.with(binding.root)
                .load(imagenPorEco(apertura.eco))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgApertura)

            binding.cardApertura.setOnClickListener {
                listenerAperturas.onClickApertura(apertura)
            }

            Thread {
                val partidasJugadas = devolverPartidasJugadas(apertura)
                binding.partidasJugadas.post {
                    binding.partidasJugadas.text = partidasJugadas
                }
            }.start()
        }
    }

    private fun devolverPartidasJugadas(apertura: Apertura):String{
        if(modo == "Favoritos"){
            return "${LiBaseApplication.database.partidasDao().gentNumeroPartidasFavoritasPorEco(apertura.eco, LoginActivity.idUsuarioActual)} " +
                    "/ ${ LiBaseApplication.database.favoritosDao().getNumeroPartidasFavoritasPorUsuario(LoginActivity.idUsuarioActual) }"
        }else if (modo == "Global"){
            return "${LiBaseApplication.database.partidasDao().getCountPartidasPorAperturaEcoVisibles(apertura.eco)} "+
                    "/ ${ LiBaseApplication.database.partidasDao().getCountPartidasVisibles()}"
        }else{
            return "${LiBaseApplication.database.partidasDao().getCountPartidasPorEco(apertura.eco, LoginActivity.idUsuarioActual)} " +
                    "/ ${ LiBaseApplication.database.partidasDao().getCountPartidas(LoginActivity.idUsuarioActual) }"
        }
    }
}