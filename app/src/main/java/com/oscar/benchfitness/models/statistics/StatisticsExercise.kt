package com.oscar.benchfitness.models.statistics

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Clase que representa la estadistica de un día de un ejercicio
 */
data class StatisticsExercise(
    val fecha: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
    val series: List<ExerciseSet> = emptyList(),
    val completado: Boolean = false
)
