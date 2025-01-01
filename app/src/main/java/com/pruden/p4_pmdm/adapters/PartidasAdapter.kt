package com.pruden.p4_pmdm.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.LoginActivity.Companion.idUsuarioActual
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import com.pruden.p4_pmdm.databinding.ItemPartidaBinding

class PartidasAdapter(val listaPartidas: MutableList<PartidaEntity>, val listenerPartidas: OnClickListenerPartidas
    ) : RecyclerView.Adapter<PartidasAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemPartidaBinding.bind(view)
    }

    private lateinit var contexto: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contexto = parent.context
        val vista = LayoutInflater.from(contexto).inflate(R.layout.item_partida, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = listaPartidas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partida = listaPartidas[position]



        with(holder){

            Thread{
                val fav = LiBaseApplication.database.favoritosDao().existeFavorito(idUsuarioActual, partida.id)
                if (fav) binding.cbFavorite.isChecked = true

                val visible = LiBaseApplication.database.visibleDao().existeVisible(idUsuarioActual, partida.id)
                if (visible) binding.cbVisible.isChecked = true

                val propietarioDeLaPartida = LiBaseApplication.database.partidasDao()
                    .getSiEsPropietarioDeLaPartida(idUsuarioActual, partida.id)

                (binding.root.context as? Activity)?.runOnUiThread {
                    if (!propietarioDeLaPartida) binding.cbVisible.visibility = View.INVISIBLE
                }

            }.start()

            binding.apertura.text = "${partida.variante} (${partida.eco})"
            binding.jugadorBlanco.text = "Blanco: ${partida.jugadorBlancas} (${partida.eloBlancas})"
            binding.jugadorNegro.text = "Negro: ${partida.jugadorNegras} (${partida.eloNegras})"
            binding.ritmoPartida.text = "Ritmo: ${partida.tiempo}"
            binding.resultado.text = "R: ${partida.resultado}"
            binding.movimientos.text = partida.movimientos
            binding.fecha.text = "Fecha de juego: ${partida.fecha.replace(".","-")}"

            binding.movimientos.setOnClickListener {
                listenerPartidas.onClickPGN(binding.movimientos)
            }
            binding.cardPartida.setOnLongClickListener {
                listenerPartidas.onLongClickPartida(partida)
                true
            }
            binding.cardPartida.setOnClickListener {
                listenerPartidas.onClickPartida(partida)
            }
            binding.apertura.setOnClickListener {
                listenerPartidas.onClickVariante(partida)
            }
            binding.linkLichess.setOnClickListener {
                listenerPartidas.onClickVerEnLichess(partida)
            }
            binding.jugadorBlanco.setOnClickListener {
                listenerPartidas.onClickBlanco(partida)
            }
            binding.jugadorNegro.setOnClickListener {
                listenerPartidas.onClickNegro(partida)
            }
            binding.movimientos.setOnLongClickListener {
                listenerPartidas.onLongClickMovimientos(partida)
                true
            }
            binding.cbFavorite.setOnClickListener {
                listenerPartidas.onClickFavorito(partida, binding.cbFavorite.isChecked)
            }
            binding.cbVisible.setOnClickListener {
                listenerPartidas.onClickVisible(partida, binding.cbVisible.isChecked)
            }

        }
    }

    fun delete(partidaEntity: PartidaEntity) {
        val indice = listaPartidas.indexOf(partidaEntity)
        if (indice != -1){
            listaPartidas.removeAt(indice)
            notifyItemRemoved(indice)
        }
    }

    fun update(partida: PartidaEntity) {
        val indice = listaPartidas.indexOf(partida)
        if (indice != -1){
            listaPartidas[indice] = partida
            notifyItemChanged(indice)
        }
    }
}