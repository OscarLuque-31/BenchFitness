package com.oscar.benchfitness.models.statistics

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Clase que representa la estadística de un día del peso
 */
data class StatisticsWeight(
    val fecha: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
    val peso: Double = 0.0,
)
