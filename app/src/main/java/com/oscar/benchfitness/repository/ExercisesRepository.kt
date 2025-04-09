package com.oscar.benchfitness.repository

import com.oscar.benchfitness.models.Categories
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.models.Levels
import com.oscar.benchfitness.models.Muscles
import com.oscar.benchfitness.services.RetrofitClient

class ExercisesRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun obtenerEjercicios(): List<ExerciseData> {
        return apiService.obtenerEjercicios()
    }

    suspend fun obtenerCategorias(): List<Categories> {
        return apiService.obtenerCategorias()
    }

    suspend fun obtenerMusculos(): List<Muscles> {
        return apiService.obtenerMusculos()
    }

    suspend fun obtenerNiveles(): List<Levels> {
        return apiService.obtenerNiveles()
    }
}
