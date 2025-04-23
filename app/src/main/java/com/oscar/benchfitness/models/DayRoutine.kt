package com.oscar.benchfitness.models

data class DayRoutine (
    val dia: String,
    val ejercicios : List<ExerciseRoutineEntry> = emptyList()
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "dia" to dia,
            "ejercicios" to ejercicios.map { it.toMap() }
        )
    }
}