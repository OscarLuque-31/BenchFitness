package com.oscar.benchfitness.models.statistics

data class ExerciseProgress(
    val nombre: String = "",
    val historial: List<StatisticsExercise> = emptyList()
)

