package com.oscar.benchfitness.viewModels.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.StatisticsExercise
import com.oscar.benchfitness.models.statistics.WeightProgress
import com.oscar.benchfitness.repository.StatisticsExercisesRepository
import com.oscar.benchfitness.repository.StatisticsWeightRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProgresoViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var progresoTotal by mutableStateOf<List<ExerciseProgress>>(emptyList())
    var filtroSeleccionado by mutableStateOf("Última semana")
    var isLoading by mutableStateOf(false)
    var ejercicioSeleccionado by mutableStateOf(ExerciseProgress())

    var historialFiltrado by mutableStateOf<List<StatisticsExercise>>(emptyList())
        private set

    // Ejercicio filtrado, con historial filtrado
    val ejercicioFiltrado: ExerciseProgress
        get() = ejercicioSeleccionado.copy(historial = historialFiltrado)

    private val statisticsProgresoRepository = StatisticsExercisesRepository(auth, db)

    fun seleccionarFiltro(nuevoFiltro: String) {
        filtroSeleccionado = nuevoFiltro
        actualizarDatosFiltrados()
    }

    fun seleccionarEjercicio(ejercicio: ExerciseProgress) {
        ejercicioSeleccionado = ejercicio
        actualizarDatosFiltrados()
    }

    fun cargarProgreso() {
        viewModelScope.launch {
            isLoading = true
            progresoTotal = statisticsProgresoRepository.getAllExerciseProgress()
            if (progresoTotal.isNotEmpty()) {
                ejercicioSeleccionado = progresoTotal.first()
            }
            actualizarDatosFiltrados()
            isLoading = false
        }
    }


    // -------------------------------------------
    // ✅ FILTRAR POR FECHAS
    // -------------------------------------------
    private fun estaEnRango(fecha: String): Boolean {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fechaParseada = try { formato.parse(fecha) } catch (e: Exception) { return false }

        val hoy = Calendar.getInstance().time
        val diferenciaDias = TimeUnit.MILLISECONDS.toDays(hoy.time - fechaParseada.time)

        return when (filtroSeleccionado) {
            "Última semana" -> diferenciaDias <= 7
            "Último mes" -> diferenciaDias <= 30
            "Último año" -> diferenciaDias <= 365
            else -> true // Todo
        }
    }


    private fun actualizarDatosFiltrados() {
        historialFiltrado = ejercicioSeleccionado.historial.filter {
            it.completado && estaEnRango(it.fecha)
        }
    }

    // -------------------------------------------
    // ✅ ESTADÍSTICAS
    // -------------------------------------------

    private fun esHoy(fecha: String): Boolean {
        val formato = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val fechaParseada = try { formato.parse(fecha) } catch (e: Exception) { return false }

        val hoy = Calendar.getInstance()
        val fechaCal = Calendar.getInstance().apply { time = fechaParseada }

        return hoy.get(Calendar.YEAR) == fechaCal.get(Calendar.YEAR)
                && hoy.get(Calendar.DAY_OF_YEAR) == fechaCal.get(Calendar.DAY_OF_YEAR)
    }

    val ejerciciosDeHoy: List<ExerciseProgress>
        get() = progresoTotal.mapNotNull { ejercicio ->
            val historialDeHoy = ejercicio.historial.filter {
                it.completado && esHoy(it.fecha)
            }
            if (historialDeHoy.isNotEmpty()) {
                ExerciseProgress(ejercicio.nombre, historialDeHoy)
            } else null
        }

    val totalEjerciciosHoy: Int
        get() = ejerciciosDeHoy.size

    val totalSeriesHoy: Int
        get() = ejerciciosDeHoy.sumOf { it.historial.sumOf { entrada -> entrada.series.size } }

    val totalRepsHoy: Int
        get() = ejerciciosDeHoy.sumOf { it.historial.sumOf { entrada -> entrada.series.sumOf { set -> set.reps } } }

    val maxPesoHoy: Double
        get() = ejerciciosDeHoy.flatMap { it.historial }
            .flatMap { it.series }
            .maxOfOrNull { it.peso } ?: 0.0

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



}