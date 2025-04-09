package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.Categories
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.models.Levels
import com.oscar.benchfitness.models.Muscles
import com.oscar.benchfitness.repository.ExercisesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EjerciciosViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val repository = ExercisesRepository()

    private val _ejercicios = MutableStateFlow<List<ExerciseData>>(emptyList())
    val ejercicios: StateFlow<List<ExerciseData>> = _ejercicios

    private val _musculos = MutableStateFlow<List<Muscles>>(emptyList())
    val musculos: StateFlow<List<Muscles>> = _musculos

    private val _niveles = MutableStateFlow<List<Levels>>(emptyList())
    val niveles: StateFlow<List<Levels>> = _niveles

    private val _categorias = MutableStateFlow<List<Categories>>(emptyList())
    val categorias: StateFlow<List<Categories>> = _categorias

    
    var musculo by mutableStateOf("Músculo")
    var categoria by mutableStateOf("Categoría")
    var nivel by mutableStateOf("Nivel")
    var busqueda by mutableStateOf("")

    var opcionesMusculo by mutableStateOf(emptyList<Muscles>())
    var opcionesCategoria by mutableStateOf(emptyList<Categories>())
    var opcionesNivel by mutableStateOf(emptyList<Levels>())

    fun cargarEjercicios() {
        viewModelScope.launch {
            try {
                _ejercicios.value = repository.obtenerEjercicios()
                _categorias.value = repository.obtenerCategorias()
                _niveles.value = repository.obtenerNiveles()
                _musculos.value = repository.obtenerMusculos()

                opcionesCategoria = _categorias.value
                opcionesNivel = _niveles.value
                opcionesMusculo = _musculos.value

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}