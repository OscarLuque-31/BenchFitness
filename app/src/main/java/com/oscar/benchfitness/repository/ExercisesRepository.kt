package com.oscar.benchfitness.repository

import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.services.RetrofitClient

class ExercisesRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun obtenerEjercicios(): List<ExerciseData> {
        return apiService.obtenerEjercicios()
    }
}
