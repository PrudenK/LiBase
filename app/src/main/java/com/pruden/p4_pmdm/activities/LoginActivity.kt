package com.pruden.p4_pmdm.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pruden.p4_pmdm.baseDatos.LiBaseApplication
import com.pruden.p4_pmdm.Metodos.comprobarContra
import com.pruden.p4_pmdm.Metodos.hashearContraConSAl
import com.pruden.p4_pmdm.Metodos.Mensajes.makeToast
import com.pruden.p4_pmdm.Metodos.cargarFondo
import com.pruden.p4_pmdm.Metodos.cargarLogo
import com.pruden.p4_pmdm.R
import com.pruden.p4_pmdm.classes.entities.UsuarioEntity
import com.pruden.p4_pmdm.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    private lateinit var preferences : SharedPreferences

    companion object{
        var idUsuarioActual = -1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cargarLogo(loginBinding.root, loginBinding.logo)
        cargarFondo(loginBinding.root, loginBinding.imgFondo)

        cargarDatosInicios()

        botonRegistrarse()
        botonInicarSesion()
        checkBox()
    }

    private fun cargarDatosInicios(){preferences = getSharedPreferences("usuario", Context.MODE_PRIVATE)
        val guadarDatos = preferences.getBoolean("sp_guardar_datos",false)
        val usuario = preferences.getString("sp_usuario", "").toString()
        val contra = preferences.getString("sp_contra", "").toString()

        val irALogin = intent.getBooleanExtra("ir_a_login", false)

        if (guadarDatos){
            loginBinding.checkBoxAutocompletar.isChecked = true
            loginBinding.tIUsuarioText.setText(usuario)
            loginBinding.tIContraText.setText(contra)

            if(!irALogin){
                iniciarSesion(usuario, contra)
            }
        }
    }

    private fun botonInicarSesion(){
        loginBinding.iniciarSesion.setOnClickListener {
            val usuario = loginBinding.tIUsuarioText.text.toString()
            val contra = loginBinding.tIContraText.text.toString()

            iniciarSesion(usuario, contra)
        }
    }

    private fun iniciarSesion(usuario : String, contra : String){
        var listaUsuarios : MutableList<UsuarioEntity> = mutableListOf()
        val thread = Thread {
            listaUsuarios = LiBaseApplication.database.usuariosDao().getAllUsuarios()
        }
        thread.start()
        thread.join()

        val logeoCorrecto = listaUsuarios.any { it.nombre == usuario && comprobarContra(contra, it.contra)}


        if(logeoCorrecto){
            val hiloId = Thread{
                idUsuarioActual = LiBaseApplication.database.usuariosDao().getIdUsuarioActual(usuario)
            }
            hiloId.start()
            hiloId.join()

            preferences.edit().putString("sp_usuario", usuario).apply()
            preferences.edit().putString("sp_contra", contra).apply()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }else{
            makeToast("Los datos introducidos son incorrectos", this)
        }
    }


    private fun botonRegistrarse(){
        loginBinding.registrarse.setOnClickListener {
            val usuario = loginBinding.tIUsuarioText.text.toString()
            val contra = loginBinding.tIContraText.text.toString()

            if(usuario.isNotBlank() && contra.isNotBlank()){
                var listaUsuarios : MutableList<UsuarioEntity> = mutableListOf()
                val thread = Thread {
                    listaUsuarios = LiBaseApplication.database.usuariosDao().getAllUsuarios()
                }
                thread.start()
                thread.join()

                val nombreRepetido = listaUsuarios.any { it.nombre == usuario }

                if (nombreRepetido){
                    makeToast("Ya ese existe el usuario: $usuario", this)
                }else{
                    val usuarioEntity = UsuarioEntity(0, usuario, hashearContraConSAl(contra))
                    Thread{
                        LiBaseApplication.database.usuariosDao().addUsuario(usuarioEntity)
                    }.start()
                    makeToast("¡Usuario registrado!", this)
                }

            }else{
                makeToast("No puedes dejar campos en blanco", this)
            }
        }
    }

    private fun checkBox(){
        loginBinding.checkBoxAutocompletar.setOnClickListener{
            if(loginBinding.checkBoxAutocompletar.isChecked){
                preferences.edit().putBoolean("sp_guardar_datos", true).apply()
                preferences.edit().putString("sp_usuario",  loginBinding.tIUsuarioText.text.toString()).apply()
                preferences.edit().putString("sp_contra",  loginBinding.tIContraText.text.toString()).apply()

                makeToast("Tus datos se guardarán", this)
            }else{
                preferences.edit().putBoolean("sp_guardar_datos", false).apply()
                makeToast("Tus datos no se guardarán", this)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {}
}