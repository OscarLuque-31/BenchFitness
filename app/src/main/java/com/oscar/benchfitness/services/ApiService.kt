package com.oscar.benchfitness.services

import com.oscar.benchfitness.models.ExerciseData
import retrofit2.http.GET

interface ApiService {
    @GET("ejercicios") // Ajusta el endpoint según tu API
    suspend fun obtenerEjercicios(): List<ExerciseData>
}