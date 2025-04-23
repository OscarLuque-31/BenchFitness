package com.oscar.benchfitness.models

data class ExerciseRoutineEntry (
    val nombre: String = "",
    val series: Int = 0,
    val repeticiones: Int = 0
) {
    // Función para convertir a Map (útil para Firebase)
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "series" to series,
            "repeticiones" to repeticiones
        )
    }
}