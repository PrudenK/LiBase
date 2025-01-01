package com.pruden.p4_pmdm.Metodos

import android.content.Context
import android.util.Log
import android.util.Patterns
import com.pruden.p4_pmdm.Metodos.Mensajes.makeToast
import com.pruden.p4_pmdm.activities.LoginActivity
import com.pruden.p4_pmdm.classes.entities.PartidaEntity
import org.jsoup.Jsoup

import java.io.IOException

fun procesarPartida(url: String, callback: (PartidaEntity?) -> Unit, context: Context, unica : Boolean = false) {
    if (!isUrlValid(url)) {
        makeToast("La URL no es válida", context)
        return
    }

    var error = false

    val hilo = Thread {
        val pgnBuilder = StringBuilder()
        try {
            val doc = Jsoup.connect(url).get()
            val moves = doc.getElementsByClass("pgn")

            val movesss = moves.text().split(Regex("\\s+(?=\\d+\\. )"))

            for (move in movesss) {
                val cleanedMove = move.replace(Regex("\\s*\\{.*?\\}"), "").trim()
                pgnBuilder.append("$cleanedMove \n")
            }

            val pgn = pgnBuilder.toString()
            val movimientos = obtenerMovimientos(pgn)

            val partida = PartidaEntity(
                0,
                LoginActivity.idUsuarioActual,
                obtenerValorPGN(pgn, "ECO"),
                ecoGeneralPorEco(obtenerValorPGN(pgn, "ECO")),
                movimientos,
                obtenerValorPGN(pgn, "TimeControl"),
                obtenerValorPGN(pgn, "UTCDate"),
                obtenerValorPGN(pgn, "Opening"),
                obtenerValorPGN(pgn, "White"),
                obtenerValorPGN(pgn, "WhiteElo"),
                obtenerValorPGN(pgn, "Black"),
                obtenerValorPGN(pgn, "BlackElo"),
                obtenerValorPGN(pgn, "Result"),
                url
            )

            callback(partida)
        } catch (e: Exception) {
            error = true
            Log.d("adsfadsf", "adsfadsfasdfadsf")
        }
    }
    hilo.start()
    if(unica){
        hilo.join()
    }

    if(error){
        makeToast("Partida no encontrada", context)
    }else{
        if(unica) {
            makeToast("Partida agregada", context)
        }
    }
}

fun isUrlValid(url: String): Boolean {
    return Patterns.WEB_URL.matcher(url).matches() && (url.startsWith("http://") || url.startsWith("https://"))
}


fun obtenerValorPGN(pgn: String, parametro: String): String {
    val regex = Regex("\\[$parametro\\s+\"(.*?)\"]")
    val matchResult = regex.find(pgn)
    return matchResult!!.groups[1]!!.value
}

fun obtenerMovimientos(pgn: String): String {
    val regex = Regex("\n1\\..*", RegexOption.DOT_MATCHES_ALL)
    val matchResult = regex.find(pgn)
    return matchResult?.value!!.replace("\n", "") ?: ""
}