package com.oscar.benchfitness.utils

fun interpretarObjetivo(objetivo: String): String {
    return when (objetivo) {
        "Perder peso" -> "Déficit"
        "Mantener peso" -> "Mantener"
        "Masa muscular" -> "Superávit"
        else -> "Desconocido"
    }
}