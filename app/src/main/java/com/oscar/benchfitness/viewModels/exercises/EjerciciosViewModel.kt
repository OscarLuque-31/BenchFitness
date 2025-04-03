package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.ExerciseData
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
    
    var musculo by mutableStateOf("Músculo")
    var categoria by mutableStateOf("Categoría")
    var nivel by mutableStateOf("Nivel")
    var busqueda by mutableStateOf("")

    var opcionesMusculo by mutableStateOf(listOf("fsadf","fsdf"))
    var opcionesCategoria by mutableStateOf(listOf("fsadf","fsdf"))
    var opcionesNivel by mutableStateOf(listOf("fsadf","fsdf"))

    fun cargarEjercicios() {
        viewModelScope.launch {
            try {
                _ejercicios.value = repository.obtenerEjercicios()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}