package com.oscar.benchfitness.services

import com.oscar.benchfitness.models.Categories
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.models.Levels
import com.oscar.benchfitness.models.Muscles
import retrofit2.http.GET

interface ApiService {
    @GET("ejercicios")
    suspend fun obtenerEjercicios(): List<ExerciseData>

    @GET("musculos")
    suspend fun obtenerMusculos(): List<Muscles>

    @GET("niveles")
    suspend fun obtenerNiveles(): List<Levels>

    @GET("categorias")
    suspend fun obtenerCategorias(): List<Categories>
}