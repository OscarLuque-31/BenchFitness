package com.oscar.benchfitness.repository

import com.oscar.benchfitness.models.exercises.Categories
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.models.exercises.Levels
import com.oscar.benchfitness.models.exercises.Muscles
import com.oscar.benchfitness.services.RetrofitClient

class ExercisesRepository {
    private val apiService = RetrofitClient.apiService

    /**
     * Método que recoge todos los ejercicios sin ningún filtro de la API
     */
    suspend fun obtenerEjercicios(): List<ExerciseData> {
        return apiService.obtenerEjercicios()
    }

    /**
     * Método que recoge todos los ejercicios con los filtros de la API
     */
    suspend fun obtenerEjerciciosConFiltro(
        nivel: String,
        categoria: String,
        musculo: String
    ): List<ExerciseData> {
        return apiService.obtenerEjerciciosFiltrados(
            nivel = nivel,
            categoria = categoria,
            musculo = musculo
        )
    }

    /**
     * Método que recoge todos los ejercicios que empiecen por el nombre filtrado
     */
    suspend fun obtenerEjerciciosPorNombre(nombre: String): List<ExerciseData> {
        return apiService.obtenerEjerciciosFiltradosPorNombre(nombre = nombre)
    }

    /**
     * Método que recoge todas las categorías
     */
    suspend fun obtenerCategorias(): List<Categories> {
        return apiService.obtenerCategorias()
    }

    /**
     * Método que recoge todos los músculos
     */
    suspend fun obtenerMusculos(): List<Muscles> {
        return apiService.obtenerMusculos()
    }

    /**
     * Método que recoge todos los niveles
     */
    suspend fun obtenerNiveles(): List<Levels> {
        return apiService.obtenerNiveles()
    }

    /**
     * Método que devuelve la URL firmada del GIF
     */
    suspend fun obtenerURLFirmadaGif(fileKey: String): String {
        return apiService.obtenerURLFirmadaGif(fileKey)
    }
}
