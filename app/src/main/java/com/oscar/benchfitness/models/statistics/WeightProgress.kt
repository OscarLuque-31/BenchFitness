package com.oscar.benchfitness.models.statistics

/**
 * Clase que representa el progreso completo del peso
 */
data class WeightProgress(
    val historial: List<StatisticsWeight> = emptyList()
)
