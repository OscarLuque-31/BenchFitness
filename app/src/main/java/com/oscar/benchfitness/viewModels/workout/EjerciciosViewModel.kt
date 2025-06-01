package com.oscar.benchfitness.viewModels.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.exercises.Categories
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.models.exercises.Levels
import com.oscar.benchfitness.models.exercises.Muscles
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
    var filtrosVisibles by mutableStateOf(false)
    var isLoading by mutableStateOf(true)

    var opcionesMusculo by mutableStateOf(emptyList<Muscles>())
    var opcionesCategoria by mutableStateOf(emptyList<Categories>())
    var opcionesNivel by mutableStateOf(emptyList<Levels>())

    /**
     * Método que carga todos los ejercicios la primera que se accede
     * a la pantalla
     */
    fun cargarEjercicios() {
        viewModelScope.launch {
            try {
                // Los filtros no serán visibles
                filtrosVisibles = false

                // Cargará la pantalla de carga
                isLoading = true

                // Se resetearan los filtros
                resetearFiltros()

                // Se obtienen los ejercicios y los datos para los filtros
                _ejercicios.value = repository.obtenerEjercicios()
                _categorias.value = repository.obtenerCategorias()
                _niveles.value = repository.obtenerNiveles()
                _musculos.value = repository.obtenerMusculos()

                // Se asignan todos los filtros
                opcionesCategoria = _categorias.value
                opcionesNivel = _niveles.value
                opcionesMusculo = _musculos.value

                // Se quita la pantalla de carga
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Método que realiza la busqueda según los filtros del usuario
     */
    fun onClickBuscar() {

        // Se parsean los filtros no utilizados
        parsearFiltros()

        // Se quita el contenido del filtro
        filtrosVisibles = false

        // Se filtra por los filtros utilizados
        filtrarBusquedaFiltros(nivel, categoria, musculo)

        // Se resetean los filtros menos los utilizadoss
        resetearFiltrosNoElegidos()
    }


    /**
     * Método que filtra la búsqueda según los filtros
     */
    private fun filtrarBusquedaFiltros(nivel: String, categoria: String, musculo: String) {
        viewModelScope.launch {
            try {
                // Pantalla de carga
                isLoading = true

                // Obtienen los ejercicios filtrados
                _ejercicios.value = repository.obtenerEjerciciosConFiltro(nivel, categoria, musculo)

                // Cargan los ejercicios
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    /**
     * Método que filtra la búsqueda según los filtros
     */
    fun filtrarBusquedaNombre(nombre: String) {
        viewModelScope.launch {
            try {
                // Pantalla de carga
                isLoading = true

                // Obtienen los ejercicios filtrados
                _ejercicios.value = repository.obtenerEjerciciosPorNombre(nombre)

                // Cargan los ejercicios
                isLoading = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    private fun parsearFiltros() {
        if (nivel == "Nivel") {
            nivel = ""
        }
        if (musculo == "Músculo") {
            musculo = ""
        }
        if (categoria == "Categoría") {
            categoria = ""
        }
    }

    private fun resetearFiltrosNoElegidos() {
        if (nivel.isEmpty()) {
            nivel = "Nivel"
        }
        if (musculo.isEmpty()) {
            musculo = "Músculo"
        }
        if (categoria.isEmpty()) {
            categoria = "Categoría"
        }
    }

    private fun resetearFiltros() {
        nivel = "Nivel"
        musculo = "Músculo"
        categoria = "Categoría"
    }
}