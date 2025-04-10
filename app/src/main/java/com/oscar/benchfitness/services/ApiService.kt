package com.oscar.benchfitness.services

import com.oscar.benchfitness.models.Categories
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.models.Levels
import com.oscar.benchfitness.models.Muscles
import com.oscar.benchfitness.navegation.Ejercicio
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("ejercicios")
    suspend fun obtenerEjercicios(): List<ExerciseData>

    @GET("musculos")
    suspend fun obtenerMusculos(): List<Muscles>

    @GET("niveles")
    suspend fun obtenerNiveles(): List<Levels>

    @GET("categorias")
    suspend fun obtenerCategorias(): List<Categories>


    @GET("ejercicios")
    suspend fun obtenerEjerciciosFiltrados(
        @Query("nivel") nivel: String = "",
        @Query("categoria") categoria: String = "",
        @Query("musculo") musculo: String = ""
    ): List<ExerciseData>

    @GET("ejercicios/search")
    suspend fun obtenerEjerciciosFiltradosPorNombre(
        @Query("nombre") nombre: String = ""
    ): List<ExerciseData>

    @GET("gifs/signed-url")
    suspend fun obtenerURLFirmadaGif(
        @Query("fileKey") fileKey: String = ""
    ): String

}