package com.oscar.benchfitness.utils

import androidx.compose.ui.graphics.Color
import com.oscar.benchfitness.ui.theme.amarilloAvanzado
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.verdePrincipiante

/**
 * Función que interpreta el objetivo del usuario
 */
fun interpretarObjetivo(objetivo: String): String {
    return when (objetivo) {
        "Perder peso" -> "Déficit"
        "Mantener peso" -> "Mantener"
        "Masa muscular" -> "Superávit"
        else -> "Desconocido"
    }
}

/**
 * Función que interpreta el color por nivel del ejercicio
 */
fun colorPorNivel(nivel: String): Color {
    return when (nivel) {
        "Principiante" -> verdePrincipiante
        "Intermedio" -> azulIntermedio
        "Avanzado" -> amarilloAvanzado
        else -> Color.White
    }
}