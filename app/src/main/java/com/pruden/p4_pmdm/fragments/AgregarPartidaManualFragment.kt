package com.pruden.p4_pmdm.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.Metodos.comprobarResultado
import com.pruden.p4_pmdm.Metodos.ecoGeneralPorEco
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.activities.HomeActivity
import com.pruden.p4_pmdm.activities.LoginActivity
import com.pruden.p4_pmdm.activities.PartidasPorAperturaActivity
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import com.pruden.p4_pmdm.databinding.FragmentAgregarPartidaManualBinding


@Suppress("DEPRECATION")
class AgregarPartidaManualFragment : Fragment() {

    private lateinit var binding: FragmentAgregarPartidaManualBinding
    private var activityHome: HomeActivity? = null

    private var partidasPorAperturaActivity: PartidasPorAperturaActivity? = null

    private var partidaEntity: PartidaEntity? = null
    private var modoEdicion: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAgregarPartidaManualBinding.inflate(inflater, container, false)

        cargarFondo()

        cargarColorHints()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPartida = arguments?.getLong(getString(R.string.id_partida_modificar), 0) ?: 0L


        if (idPartida == 0L) {
            binding.tituloToolBar.text = "Agregar partida"
            modoEdicion = false
            partidaEntity =
                PartidaEntity(id = 0L, -1L, "", "", "", "", "", "", "", "", "", "", "", "")
            activityHome = activity as HomeActivity
        } else {
            modoEdicion = true
            binding.tituloToolBar.text = "Modificar partida"
            partidasPorAperturaActivity = activity as PartidasPorAperturaActivity
            cargarValoresPartidaModificar(idPartida)
            cargarCamposPartidaModificar()
        }


        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)



        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
        }

        val back = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        back?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))

        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(back)

        val check = ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
        check?.setTint(ContextCompat.getColor(requireContext(), R.color.lightGrayColor))


        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tool_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
                true
            }

            R.id.subir_partida -> {

                if (partidaEntity != null) {
                    with(partidaEntity!!) {
                        idUsuario = LoginActivity.idUsuarioActual
                        eco = binding.inputEco.text.toString()
                        ecoGeneral = ecoGeneralPorEco(binding.inputEco.text.toString())
                        movimientos = binding.inputMovimientos.text.toString()
                        tiempo = binding.inputTiempo.text.toString()
                        fecha = binding.inputFecha.text.toString()
                        variante = binding.inputVariante.text.toString()
                        jugadorBlancas = binding.inputJugadorBlancas.text.toString()
                        eloBlancas = binding.inputEloBlancas.text.toString()
                        jugadorNegras = binding.inputJugadorNegras.text.toString()
                        eloNegras = binding.inputEloNegras.text.toString()
                        resultado = binding.inputResultado.text.toString()
                        url = ""
                    }


                    if(validarCampos(eco = binding.tilEco, resultado = binding.tilResultado, 
                            binding.tilMovimientos, binding.tilFecha, binding.tilVariante,
                            binding.tilJugadorBlancas,binding.tilEloBlancas, binding.tilJugadorNegras,
                        binding.tilEloNegras, binding.tilTiempo
                    )){
                        Thread {
                            if (modoEdicion) {
                                LiBaseApplication.database.partidasDao().updatePartida(partidaEntity!!)
                            }
                            requireActivity().runOnUiThread {
                                if (modoEdicion) {
                                    partidasPorAperturaActivity!!.updatePartidaAux(partidaEntity!!)

                                    Toast.makeText(partidasPorAperturaActivity,
                                        R.string.partida_modificada, Toast.LENGTH_SHORT).show()
                                    partidasPorAperturaActivity!!.onBackPressed()
                                } else {
                                    activityHome!!.addPartidaAux(partidaEntity!!)
                                    Toast.makeText(activityHome, "Partida añadida con éxito", Toast.LENGTH_SHORT).show()
                                    activityHome?.onBackPressed()
                                }
                            }
                        }.start()
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroy() {
        if (activityHome != null) {
            activityHome!!.gestionFab(true)
        }

        super.onDestroy()
    }


    private fun cargarValoresPartidaModificar(idPartida: Long) {
        val hilo = Thread {
            partidaEntity = LiBaseApplication.database.partidasDao().getPartidaPorId(idPartida)
        }
        hilo.start()
        hilo.join()
    }

    private fun cargarCamposPartidaModificar() {
        with(binding) {
            inputEco.setText(partidaEntity!!.eco)
            inputMovimientos.setText(partidaEntity!!.movimientos)
            inputTiempo.setText(partidaEntity!!.tiempo)
            inputFecha.setText(partidaEntity!!.fecha)
            inputVariante.setText(partidaEntity!!.variante)
            inputJugadorBlancas.setText(partidaEntity!!.jugadorBlancas)
            inputEloBlancas.setText(partidaEntity!!.eloBlancas)
            inputJugadorNegras.setText(partidaEntity!!.jugadorNegras)
            inputEloNegras.setText(partidaEntity!!.eloNegras)
            inputResultado.setText(partidaEntity!!.resultado)
        }
    }

    private fun validarCampos(eco : TextInputLayout, resultado : TextInputLayout, vararg campos: TextInputLayout): Boolean {
        var valido = true
        if(ecoGeneralPorEco(eco.editText?.text.toString()) == "Rango desconocido"){
            Snackbar.make(binding.root, "Rango Eco incorrecto, A00-E99", Snackbar.LENGTH_SHORT).show()
            eco.error = "Eco: A00-E99"
            eco.editText?.requestFocus()
            valido = false
        }else{
            eco.error = null
            for (textField in campos) {
                if (textField.editText?.text.toString().trim().isEmpty()) {
                    textField.error = "Error"
                    textField.editText?.requestFocus()
                    valido = false
                } else {
                    textField.error = null
                }
            }
            if (!valido) {
                Snackbar.make(binding.root, "No pueden haber campos en blanco", Snackbar.LENGTH_SHORT).show()
            }
        }

        if(valido){
            if(!comprobarResultado(resultado.editText?.text.toString())){
                Snackbar.make(binding.root, "Resultado incorrecto", Snackbar.LENGTH_SHORT).show()
                resultado.error = "1-0, 0-1 o 1/2-1/2"
                resultado.editText?.requestFocus()
                valido = false
            }
        }

        return valido
    }

    private fun cargarColorHints(){
        val textInputLayouts = listOf(
            binding.tilEco,
            binding.tilMovimientos,
            binding.tilFecha,
            binding.tilVariante,
            binding.tilJugadorBlancas,
            binding.tilEloBlancas,
            binding.tilJugadorNegras,
            binding.tilEloNegras,
            binding.tilTiempo,
            binding.tilResultado
        )

        textInputLayouts.forEach { til ->
            til.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                R.color.lightGrayColor
            ))
        }
    }

    private fun cargarFondo(){
        Glide.with(binding.root)
            .load(R.drawable.fondo)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imgFondo)
    }
}
