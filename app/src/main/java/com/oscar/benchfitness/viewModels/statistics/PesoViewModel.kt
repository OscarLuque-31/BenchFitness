package com.oscar.benchfitness.viewModels.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.StatisticsWeight
import com.oscar.benchfitness.models.statistics.WeightProgress
import com.oscar.benchfitness.repository.StatisticsWeightRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class PesoViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var progreso by mutableStateOf<WeightProgress?>(WeightProgress())
    var peso by mutableStateOf("")
    var ultimoPeso by mutableStateOf("")
    var mostrarDialogoConfirmacion by mutableStateOf(false)
    var mostrarDialogoAgregarPeso by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var filtroSeleccionado by mutableStateOf("Último mes")
        private set
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var datosFiltrados by mutableStateOf<List<Double>>(emptyList())
        private set

    var pesoInvalido by mutableStateOf(false)

    private val statisticsWeightRepository = StatisticsWeightRepository(auth, db)
    private val userRepository = UserRepository(auth, db)

    fun obtenerUltimoPeso(): String {
        val historial = obtenerHistorialOrdenado() ?: return "--"

        if (historial.isEmpty()) return "--"

        ultimoPeso = historial.last().first.peso.toString()
        return ultimoPeso
    }

    fun obtenerDiferenciaPeso(): String {
        val historial = progreso?.historial
            ?.mapNotNull {
                try {
                    it to LocalDate.parse(it.fecha, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }
            ?.sortedBy { it.second }
            ?: return "--"

        if (historial.size < 2) return "--"

        val pesoActual = historial.last().first.peso
        val pesoAnterior = historial[historial.size - 2].first.peso
        val diferencia = pesoActual - pesoAnterior

        val signo = when {
            diferencia > 0 -> "+"
            diferencia < 0 -> "-"
            else -> ""
        }

        return "$signo%.1f kg".format(abs(diferencia))
    }


    fun obtenerEstadoPeso(): Color {
        val historial = obtenerHistorialOrdenado()
            ?: return Color.Gray

        if (historial.size < 2) return Color.Gray

        val pesoActual = historial.last().first.peso
        val pesoAnterior = historial[historial.size - 2].first.peso

        return if (pesoActual > pesoAnterior) rojoBench else verdePrincipiante
    }

    private fun obtenerHistorialOrdenado(): List<Pair<StatisticsWeight, LocalDate>>? {
        val historial = progreso?.historial
            ?.mapNotNull {
                try {
                    it to LocalDate.parse(it.fecha, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }
            ?.sortedBy { it.second }

        return historial
    }


    fun seleccionarFiltro(nuevoFiltro: String) {
        filtroSeleccionado = nuevoFiltro
        datosFiltrados = actualizarDatosFiltrados()
    }

    private fun actualizarDatosFiltrados(): List<Double> {
        val historialCompleto = progreso?.historial ?: return emptyList()

        val desde = when (filtroSeleccionado) {
            "Última semana" -> LocalDate.now().minusDays(7)
            "Último mes" -> LocalDate.now().minusMonths(1)
            "Último año" -> LocalDate.now().minusYears(1)
            else -> LocalDate.MIN
        }

        return historialCompleto
            .mapNotNull { entrada ->
                try {
                    val fechaLocal = LocalDate.parse(entrada.fecha, dateFormatter)
                    if (fechaLocal.isAfter(desde)) {
                        entrada to fechaLocal
                    } else null
                } catch (e: Exception) {
                    null
                }
            }
            .sortedBy { it.second }
            .map { it.first.peso }
    }

    suspend fun cargarHistorialPesos() {
        viewModelScope.launch {
            progreso = statisticsWeightRepository.getAllWeightProgress()
            datosFiltrados = actualizarDatosFiltrados()
        }
    }

    fun abrirAgregarPeso() {
        mostrarDialogoAgregarPeso = true
    }

    fun cerrarAgregarPeso() {
        mostrarDialogoAgregarPeso = false
        peso = ""
        pesoInvalido = false
    }

    fun confirmarAgregarPeso() {
        isLoading = true
        mostrarDialogoConfirmacion = false
        mostrarDialogoAgregarPeso = false

        viewModelScope.launch {
            try {
                val fechaHoy = LocalDate.now().format(dateFormatter)
                val nuevoPeso = StatisticsWeight(
                    peso = peso.toDouble(),
                    fecha = fechaHoy
                )

                statisticsWeightRepository.saveWeightExecution(newProgress = nuevoPeso)
                userRepository.asignarPeso(peso = peso)
                cargarHistorialPesos()
            } finally {
                isLoading = false
                cerrarAgregarPeso()
            }
        }
    }
}
