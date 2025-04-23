package com.oscar.benchfitness.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Routine (
    val nombre: String,
    val objetivo: String,
    val dias: List<DayRoutine>,
    val fechaCreacion: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "objetivo" to objetivo,
            "dias" to dias.map { it.toMap() },
            "fechaCreacion" to fechaCreacion
        )
    }
}