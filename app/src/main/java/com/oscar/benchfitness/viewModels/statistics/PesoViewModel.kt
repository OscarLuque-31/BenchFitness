package com.oscar.benchfitness.viewModels.statistics


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.StatisticsWeight
import com.oscar.benchfitness.models.statistics.WeightProgress
import com.oscar.benchfitness.repository.StatisticsExercisesRepository
import com.oscar.benchfitness.repository.StatisticsWeightRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import kotlin.math.abs

class PesoViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var progreso by mutableStateOf<WeightProgress?>(WeightProgress())
    var peso by mutableStateOf("")
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



    // Función simplificada para obtener diferencia
    fun obtenerDiferenciaPeso(): String {
        val historial = progreso?.historial?.sortedBy { it.fecha } ?: return "--"
        if (historial.size < 2) return "--"

        val diferencia = historial.last().peso - historial[historial.size - 2].peso

        val signo = when {
            diferencia > 0 -> "+"
            diferencia < 0 -> "-" // el número ya será negativo, pero así lo haces explícito
            else -> ""
        }

        return "$signo%.1f kg".format(abs(diferencia))
    }


    // Función para saber si aumentó o bajó
    fun obtenerEstadoPeso(): Color {
        val historial = progreso?.historial?.sortedBy { it.fecha } ?: return Color.Gray
        if (historial.size < 2) return Color.Gray

        val diferencia = historial.last().peso - historial[historial.size - 2].peso
        return if (diferencia > 0) rojoBench else verdePrincipiante
    }



    fun seleccionarFiltro(nuevoFiltro: String) {
        filtroSeleccionado = nuevoFiltro
        datosFiltrados = actualizarDatosFiltrados()
    }


    fun actualizarDatosFiltrados(): List<Double> {
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
                        entrada
                    } else null
                } catch (e: Exception) {
                    null // por si alguna fecha está mal formateada
                }
            }
            .sortedBy { LocalDate.parse(it.fecha, dateFormatter) }
            .map { it.peso }
    }

    suspend fun cargarHistorialPesos(){
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
                statisticsWeightRepository.saveWeightExecution(
                    newProgress = StatisticsWeight(peso = peso.toDouble())
                )
                userRepository.asignarPeso(peso = peso)
                cargarHistorialPesos()
            } finally {
                isLoading = false
                cerrarAgregarPeso()
            }
        }
    }

}