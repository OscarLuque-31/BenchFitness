package com.oscar.benchfitness.viewModels.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.StatisticsExercise
import com.oscar.benchfitness.repository.StatisticsExercisesRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProgresoViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    // Lista completa del progreso de todos los ejercicios
    var progresoTotal by mutableStateOf<List<ExerciseProgress>>(emptyList())

    // Ejercicio que se está visualizando actualmente
    var ejercicioSeleccionado by mutableStateOf(ExerciseProgress())

    // Historial filtrado según el filtro y el ejercicio seleccionado
    var historialFiltrado by mutableStateOf<List<StatisticsExercise>>(emptyList())
        private set

    var filtroSeleccionado by mutableStateOf("Última semana")
    var isLoading by mutableStateOf(false)

    // Filtra ejercicios completados hoy
    private val ejerciciosDeHoy: List<ExerciseProgress>
        get() = progresoTotal.mapNotNull { ejercicio ->
            val historialDeHoy = ejercicio.historial.filter {
                it.completado && esHoy(it.fecha)
            }
            if (historialDeHoy.isNotEmpty()) {
                ExerciseProgress(ejercicio.nombre, historialDeHoy)
            } else null
        }

    // Cantidad de ejercicios realizados hoy
    val totalEjerciciosHoy: Int
        get() = ejerciciosDeHoy.size

    // Total de series completadas hoy
    val totalSeriesHoy: Int
        get() = ejerciciosDeHoy.sumOf { it.historial.sumOf { entrada -> entrada.series.size } }

    // Total de repeticiones realizadas hoy
    val totalRepsHoy: Int
        get() = ejerciciosDeHoy.sumOf { it.historial.sumOf { entrada -> entrada.series.sumOf { set -> set.reps } } }

    // Peso máximo levantado hoy (en cualquier serie)
    val maxPesoHoy: Double
        get() = ejerciciosDeHoy.flatMap { it.historial }
            .flatMap { it.series }
            .maxOfOrNull { it.peso } ?: 0.0

    // Ejercicio con mayor volumen de carga hoy (peso x repeticiones)
    val ejercicioTopHoy: String?
        get() {
            val volumenPorEjercicio = ejerciciosDeHoy.map { ejercicio ->
                val volumen = ejercicio.historial
                    .flatMap { it.series }
                    .sumOf { (it.reps * it.peso).toInt() }
                ejercicio.nombre to volumen
            }

            return volumenPorEjercicio.maxByOrNull { it.second }?.first
        }

    // Repositorio para obtener estadísticas de ejercicios
    private val statisticsProgresoRepository = StatisticsExercisesRepository(auth, db)

    /**
     * Método que cambia el filtro seleccionado
     */
    fun seleccionarFiltro(nuevoFiltro: String) {
        filtroSeleccionado = nuevoFiltro
        actualizarDatosFiltrados()
    }

    /**
     * Método que cambia el ejercicio seleccionado
     */
    fun seleccionarEjercicio(ejercicio: ExerciseProgress) {
        ejercicioSeleccionado = ejercicio
        actualizarDatosFiltrados()
    }

    /**
     * Método que carga el progreso de todos los ejercicios en base de datos
     */
    fun cargarProgreso() {
        viewModelScope.launch {
            isLoading = true
            // Cargar todos los ejercicios con su historial
            progresoTotal = statisticsProgresoRepository.getAllExerciseProgress()

            // Selecciona automáticamente el primer ejercicio
            if (progresoTotal.isNotEmpty()) {
                ejercicioSeleccionado = progresoTotal.first()
            }

            // Aplica el filtro al historial del ejercicio seleccionado
            actualizarDatosFiltrados()
            isLoading = false
        }
    }

    /**
     * Verifica si una fecha está dentro del rango del filtro activo
     */
    private fun estaEnRangoConFiltro(fecha: String, filtro: String): Boolean {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fechaParseada = try {
            formato.parse(fecha)
        } catch (e: Exception) {
            return false
        }

        val hoy = Calendar.getInstance().time
        val diferenciaDias = TimeUnit.MILLISECONDS.toDays(hoy.time - fechaParseada.time)

        return when (filtro) {
            "Última semana" -> diferenciaDias <= 7
            "Último mes" -> diferenciaDias <= 30
            "Último año" -> diferenciaDias <= 365
            else -> true
        }
    }

    /**
     * Filtra el historial del ejercicio seleccionado en base al filtro de fechas
     */
    private fun actualizarDatosFiltrados() {
        historialFiltrado = ejercicioSeleccionado.historial.filter {
            it.completado && estaEnRangoConFiltro(it.fecha, filtroSeleccionado)
        }
    }

    /**
     * Comprueba si la fecha recibida corresponde al día actual
     */
    private fun esHoy(fecha: String): Boolean {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fechaParseada = try {
            formato.parse(fecha)
        } catch (e: Exception) {
            return false
        }

        val hoy = Calendar.getInstance()
        val fechaCal = Calendar.getInstance().apply { time = fechaParseada }

        return hoy.get(Calendar.YEAR) == fechaCal.get(Calendar.YEAR)
                && hoy.get(Calendar.DAY_OF_YEAR) == fechaCal.get(Calendar.DAY_OF_YEAR)
    }
}