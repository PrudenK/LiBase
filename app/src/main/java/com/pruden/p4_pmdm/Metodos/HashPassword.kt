package com.pruden.p4_pmdm.Metodos

import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

fun hashearContraConSAl(contra: String): String {
    val salt = generarSal()
    val hashedPassword = hashPassword(contra, salt)
    val saltString = Base64.encodeToString(salt, Base64.NO_WRAP)
    return "$saltString:$hashedPassword"
}

fun comprobarContra(contraIntro: String, contraHash: String): Boolean {
    val parts = contraHash.split(":")
    if (parts.size != 2) {
        return false
    }
    val salt = Base64.decode(parts[0], Base64.NO_WRAP)
    val contraHasheada = parts[1]
    val inputHasheada = hashPassword(contraIntro, salt)
    return contraHasheada == inputHasheada
}

private fun generarSal(length: Int = 16): ByteArray {
    val salt = ByteArray(length)
    SecureRandom().nextBytes(salt)
    return salt
}

private fun hashPassword(contra: String, salt: ByteArray): String {
    val md = MessageDigest.getInstance("SHA-256")
    val saltedPassword = salt + contra.toByteArray()
    val hashedPassword = md.digest(saltedPassword)
    return Base64.encodeToString(hashedPassword, Base64.NO_WRAP)
}
