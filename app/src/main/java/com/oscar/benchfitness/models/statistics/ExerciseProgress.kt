package com.oscar.benchfitness.models.statistics

/**
 * Clase que representa el progreso de un ejercicio
 */
data class ExerciseProgress(
    val nombre: String = "",
    val historial: List<StatisticsExercise> = emptyList()
)

