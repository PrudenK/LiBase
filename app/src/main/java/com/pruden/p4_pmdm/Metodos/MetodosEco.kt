package com.pruden.p4_pmdm.Metodos

import com.pruden.p4_pmdm.R

fun nombrePorEco(eco: String): String {
    return when (eco) {
        in "A00".."A09", "A00-A09" -> "Aperturas Irregulares"
        in "A10".."A39", "A10-A39" -> "Aperturas inglesas"
        in "A40".."A99", "A40-A99" -> "Peón de dama"
        in "B00".."B09", "B00-B09" -> "Defensas modernas"
        in "B10".."B19", "B10-B19" -> "Defensa Caro-Kann"
        in "B20".."B99", "B20-B99" -> "Defensa Siciliana"
        in "C00".."C19", "C00-C19" -> "Defensa Francesa"
        in "C20".."C59", "C20-C59" -> "Abiertas Clásicas"
        in "C60".."C99", "C60-C99" -> "Apertura Española"
        in "D00".."D05", "D00-D05" -> "Colle y Peón de Dama"
        in "D06".."D19", "D06-D19" -> "Defensa Eslava"
        in "D20".."D29", "D20-D29" -> "Gambito de Dama"
        in "D30".."D69", "D30-D69" -> "Defensas Semieslavas"
        in "D70".."D99", "D70-D99" -> "Grünfeld y variantes"
        in "E00".."E19", "E00-E19" -> "Apertura Catalana"
        in "E20".."E59", "E20-E59" -> "Defensa Nimzoindia"
        in "E60".."E99", "E60-E99" -> "India de Rey y Benoni"
        else -> "Código ECO desconocido"
    }
}

fun descripcionPorEco(eco: String): String {
    return when (eco) {
        in "A00".."A09", "A00-A09" -> "Aperturas irregulares y no tradicionales, fuera de los sistemas estándar."
        in "A10".."A39", "A10-A39" -> "Aperturas inglesas, simétricas, declinada y defensivas Caro-Kann."
        in "A40".."A99", "A40-A99" -> "Defensas holandesas, sistemas Colle y otras variantes poco comunes."
        in "B00".."B09", "B00-B09" -> "Defensas modernas y sistemas flexibles como la Pirc."
        in "B10".."B19", "B10-B19" -> "Defensa Caro-Kann con variantes sólidas y cerradas."
        in "B20".."B99", "B20-B99" -> "Defensa Siciliana, sistema dinámico y popular para contrarrestar e4."
        in "C00".."C19", "C00-C19" -> "Defensa Francesa, lucha estratégica contra e4 con peones cerrados."
        in "C20".."C59", "C20-C59" -> "Aperturas abiertas clásicas, enfocadas en desarrollo rápido y control central."
        in "C60".."C99", "C60-C99" -> "Defensa Española, una de las aperturas más antiguas y estudiadas."
        in "D00".."D05", "D00-D05" -> "Sistemas Colle y aperturas lentas de Peón de Dama."
        in "D06".."D19", "D06-D19" -> "Defensa Eslava, enfocada en el desarrollo sólido y control central."
        in "D20".."D29", "D20-D29" -> "Gambito de Dama Aceptado, cediendo centro para buscar contrajuego."
        in "D30".."D69", "D30-D69" -> "Defensas Semieslavas, sistemas sólidos para contrarrestar d4 c4."
        in "D70".."D99", "D70-D99" -> "Defensa Grünfeld, contrajuego dinámico y enfoque en el centro abierto."
        in "E00".."E19", "E00-E19" -> "Defensa Catalana, combina ideas abiertas y estructuras sólidas."
        in "E20".."E59", "E20-E59" -> "Defensa Nimzoindia, control central con presión en peones débiles."
        in "E60".."E99", "E60-E99" -> "Defensa India de Rey, lucha dinámica contra d4 con fianchettos."
        else -> "Descripción no disponible para este rango de ECO."
    }
}

fun imagenPorEco(eco: String): Int {
    return when (eco) {
        in "A00".."A09", "A00-A09" -> R.mipmap.ic_a00_a09_foreground
        in "A10".."A39", "A10-A39" -> R.mipmap.ic_a10_a39_foreground
        in "A40".."A99", "A40-A99" -> R.mipmap.ic_a40_a99_foreground
        in "B00".."B09", "B00-B09" -> R.mipmap.ic_b00_b09_foreground
        in "B10".."B19", "B10-B19" -> R.mipmap.ic_b10_b19_foreground
        in "B20".."B99", "B20-B99" -> R.mipmap.ic_b20_b99_foreground
        in "C00".."C19", "C00-C19" -> R.mipmap.ic_c00_c19_foreground
        in "C20".."C59", "C20-C59" -> R.mipmap.ic_c20_c59_foreground
        in "C60".."C99", "C60-C99" -> R.mipmap.ic_c60_c99_foreground
        in "D00".."D05", "D00-D05" -> R.mipmap.ic_d00_d05_foreground
        in "D06".."D19", "D06-D19" -> R.mipmap.ic_d06_d19_foreground
        in "D20".."D29", "D20-D29" -> R.mipmap.ic_d20_d29_foreground
        in "D30".."D69", "D30-D69" -> R.mipmap.ic_d30_d69_foreground
        in "D70".."D99", "D70-D99" -> R.mipmap.ic_d70_d99_foreground
        in "E00".."E19", "E00-E19" -> R.mipmap.ic_e00_e19_foreground
        in "E20".."E59", "E20-E59" -> R.mipmap.ic_e20_e59_foreground
        in "E60".."E99", "E60-E99" -> R.mipmap.ic_e60_e99_foreground
        else -> -1
    }
}

fun ecoGeneralPorEco(eco: String): String {
    val regex = Regex("^[A-E][0-9]{2}$")
    if (!regex.matches(eco)) {
        return "Rango desconocido"
    }

    return when (eco) {
        in "A00".."A09" -> "A00-A09"
        in "A10".."A39" -> "A10-A39"
        in "A40".."A99" -> "A40-A99"
        in "B00".."B09" -> "B00-B09"
        in "B10".."B19" -> "B10-B19"
        in "B20".."B99" -> "B20-B99"
        in "C00".."C19" -> "C00-C19"
        in "C20".."C59" -> "C20-C59"
        in "C60".."C99" -> "C60-C99"
        in "D00".."D05" -> "D00-D05"
        in "D06".."D19" -> "D06-D19"
        in "D20".."D29" -> "D20-D29"
        in "D30".."D69" -> "D30-D69"
        in "D70".."D99" -> "D70-D99"
        in "E00".."E19" -> "E00-E19"
        in "E20".."E59" -> "E20-E59"
        in "E60".."E99" -> "E60-E99"
        else -> "Rango desconocido"
    }
}

fun comprobarResultado(resultado : String):Boolean{
    return resultado == "1-0" || resultado == "0-1" || resultado == "1/2-1/2"
}