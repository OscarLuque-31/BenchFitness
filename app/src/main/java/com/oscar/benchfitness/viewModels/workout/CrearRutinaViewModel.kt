package com.oscar.benchfitness.viewModels.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.DayRoutine
import com.oscar.benchfitness.models.routines.ExerciseRoutineEntry
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.repository.ExercisesRepository
import com.oscar.benchfitness.repository.RoutineRepository
import kotlinx.coroutines.launch

class CrearRutinaViewModel(auth: FirebaseAuth, db: FirebaseFirestore) : ViewModel() {


    val MAX_NOMBRE_RUTINA_LENGTH = 15



    var nombreRutina by mutableStateOf("")
    var objetivo by mutableStateOf("Objetivo")
    var diasSeleccionados by mutableStateOf<List<String>>(listOf())
    var diaSeleccionado by mutableStateOf("")
    var ejerciciosPorDia = mutableStateMapOf<String, List<ExerciseRoutineEntry>>()
    var ejercicioSeleccionado by mutableStateOf<ExerciseRoutineEntry?>(null)
    var nombreEjercicioSeleccionado by mutableStateOf("")
    var listaEjercicios by mutableStateOf<List<String>>(emptyList())
    var showDialog by mutableStateOf(false)
    var recomendaciones by mutableStateOf("")
    var errorRutina by mutableStateOf<String?>(null)


    // Variables para los datos del ejercicio
    var ejercicio by mutableStateOf(ExerciseRoutineEntry())

    var seriesText by mutableStateOf("")
    var repeticionesText by mutableStateOf("")

    private val repository = ExercisesRepository()
    private val routineRepository = RoutineRepository(auth, db)


    /**
     * Función para obtener las recomendaciones según el objetivo
     */
    fun obtenerRecomendacionesPorObjetivo() {
        recomendaciones = when (objetivo) {
            "Hipertrofia" -> {
                "Para hipertrofia:\n" +
                        "- Series: 3-5 series por ejercicio\n" +
                        "- Repeticiones: 6-12 repeticiones\n" +
                        "- Ejercicios compuestos y de aislamiento"
            }

            "Fuerza" -> {
                "Para fuerza:\n" +
                        "- Series: 4-6 series por ejercicio\n" +
                        "- Repeticiones: 1-5 repeticiones\n" +
                        "- Ejercicios compuestos (sentadillas, press de banca, etc.)"
            }

            else -> "Selecciona un objetivo para recibir recomendaciones."
        }
    }

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

                if (nombreRutinaYaExiste(nombreRutina)) {
                    errorRutina = "Ya existe una rutina con este nombre."
                    return@launch
                }

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
    fun agregarEjercicioAlDia(dia: String, entry: ExerciseRoutineEntry): Boolean {
        return if (ejercicioYaExisteEnDia(dia, entry.nombre)) {
            false
        } else {
            val listaActual = ejerciciosPorDia[dia]?.toMutableList() ?: mutableListOf()
            listaActual.add(entry)
            ejerciciosPorDia[dia] = listaActual
            true
        }
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


    // Método para validar los ejercicios según el objetivo
    fun validarEjercicio(dia: String): String? {
        if (nombreEjercicioSeleccionado.isBlank()) {
            return "Selecciona un ejercicio."
        }

        val seriesValid = seriesText.toIntOrNull()
        val repeticionesValid = repeticionesText.toIntOrNull()

        if (seriesValid == null || repeticionesValid == null) {
            return "Series y repeticiones deben ser números válidos."
        }

        if (seriesValid <= 0 || repeticionesValid <= 0) {
            return "Series y repeticiones deben ser mayores que cero."
        }

        // Validación de ejercicio duplicado
        if (ejercicioYaExisteEnDia(dia, nombreEjercicioSeleccionado)) {
            return "Este ejercicio ya está agregado para este día."
        }

        return null
    }

    fun obtenerRecomendacionActual(): String? {
        val series = seriesText.toIntOrNull() ?: return null
        val repes = repeticionesText.toIntOrNull() ?: return null

        return when (objetivo) {
            "Hipertrofia" -> {
                val recSeries = if (series < 3 || series > 5) "3-5 series" else null
                val recRepes = if (repes < 6 || repes > 12) "6-12 repeticiones" else null
                listOfNotNull(recSeries, recRepes).takeIf { it.isNotEmpty() }
                    ?.joinToString("\n")
                    ?.let { "Recomendación para hipertrofia:\n$it" }
            }

            "Fuerza" -> {
                val recSeries = if (series < 4 || series > 6) "4-6 series" else null
                val recRepes = if (repes < 1 || repes > 5) "1-5 repeticiones" else null
                listOfNotNull(recSeries, recRepes).takeIf { it.isNotEmpty() }
                    ?.joinToString("\n")
                    ?.let { "Recomendación para fuerza:\n$it" }
            }

            else -> null
        }
    }

    private fun ejercicioYaExisteEnDia(dia: String, nombreEjercicio: String): Boolean {
        return ejerciciosPorDia[dia]?.any { it.nombre == nombreEjercicio } ?: false
    }

    fun seleccionarEjercicio(nombre: String) {
        nombreEjercicioSeleccionado = nombre
    }

    fun deseleccionarEjercicio() {
        nombreEjercicioSeleccionado = ""
    }

    fun crearEntryEjercicio(): ExerciseRoutineEntry {
        return ExerciseRoutineEntry(
            nombre = nombreEjercicioSeleccionado,
            series = seriesText.toInt(),
            repeticiones = repeticionesText.toInt()
        )
    }

    fun resetFormularioEjercicio() {
        nombreEjercicioSeleccionado = ""
        seriesText = ""
        repeticionesText = ""
    }

    fun validarRutina(): Boolean {
        errorRutina = when {
            nombreRutina.isBlank() -> "El nombre de la rutina no puede estar vacío."
            nombreRutina.length > MAX_NOMBRE_RUTINA_LENGTH ->
                "El nombre no puede tener más de $MAX_NOMBRE_RUTINA_LENGTH caracteres."
            objetivo == "Objetivo" -> "Debes seleccionar un objetivo válido."
            diasSeleccionados.isEmpty() -> "Debes seleccionar al menos un día."
            diasSeleccionados.any { (ejerciciosPorDia[it]?.size ?: 0) < 3 } ->
                "Todos los días seleccionados deben tener al menos 3 ejercicios."

            else -> null
        }

        return errorRutina == null
    }

    private suspend fun nombreRutinaYaExiste(nombre: String): Boolean {
        return try {
            routineRepository.getAllRoutines().any { it.nombre.equals(nombre, ignoreCase = true) }
        } catch (e: Exception) {
            false // Por defecto no bloquear si falla la verificación, o ajusta según tu lógica
        }
    }

}
