package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.DayRoutine
import com.oscar.benchfitness.models.ExerciseRoutineEntry
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.repository.ExercisesRepository
import com.oscar.benchfitness.repository.RoutineRepository
import kotlinx.coroutines.launch
import java.util.UUID

class CrearRutinaViewModel (auth: FirebaseAuth, db: FirebaseFirestore): ViewModel() {

    var nombreRutina by mutableStateOf("")
    var objetivo by mutableStateOf("Objetivo")
    var diasSeleccionados by mutableStateOf<List<String>>(listOf())
    var diaSeleccionado by mutableStateOf("")
    var ejerciciosPorDia = mutableStateMapOf<String, List<ExerciseRoutineEntry>>()
    var ejercicioSeleccionado by mutableStateOf<ExerciseRoutineEntry?>(null)
    var nombreEjercicioSeleccionado by mutableStateOf("")
    var listaEjercicios by mutableStateOf<List<String>>(emptyList())
    var showDialog by mutableStateOf(false)


    // Variables para los datos del ejercicio
    var ejercicio by mutableStateOf(ExerciseRoutineEntry())
    var series by mutableIntStateOf(0)
    var repeticiones by mutableIntStateOf(0)

    private val repository = ExercisesRepository()
    private val routineRepository = RoutineRepository(auth,db)


    private fun crearRutinaCompleta(): Routine {
        val diasRutina = diasSeleccionados.map { dia ->
            DayRoutine(
                dia = dia,
                ejercicios = ejerciciosPorDia[dia] ?: emptyList()
            )
        }

        return Routine(
            nombre = nombreRutina,
            objetivo = objetivo,
            dias = diasRutina
        )
    }

    fun guardarRutinaEnFirebase(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val routine = crearRutinaCompleta()
                val id = routineRepository.saveRoutine(routine)
                onSuccess(id)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }


    /**
     * Método que agrega ejercicios a un día específico
     */
    fun agregarEjercicioAlDia(dia: String, entry: ExerciseRoutineEntry) {
        val listaActual = ejerciciosPorDia[dia]?.toMutableList() ?: mutableListOf()
        listaActual.add(entry)
        ejerciciosPorDia[dia] = listaActual
    }

    fun eliminarEjercicioDelDia(dia: String, entry: ExerciseRoutineEntry) {
        val listaActual = ejerciciosPorDia[dia]?.toMutableList() ?: return
        listaActual.remove(entry)
        ejerciciosPorDia[dia] = listaActual
    }

    fun obtenerNombreEjercicios() {
        viewModelScope.launch {
            try {
                // Realizamos la llamada a la API para obtener los ejercicios
                val ejercicios = repository.obtenerEjercicios()

                // Actualizamos la lista de ejercicios con los datos obtenidos
                listaEjercicios = ejercicios.map { it.nombre }

            } catch (e: Exception) {
                // Manejo de errores (puedes mostrar un mensaje de error si es necesario)
                println("Error al obtener los ejercicios: ${e.message}")
            }
        }
    }

    // Resetea los valores del formulario de ejercicios
    fun resetFormularioEjercicio() {
        ejercicio = ExerciseRoutineEntry()
    }
}
