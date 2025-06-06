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

    // Variables necesarias para las estadisticas de peso
    var progreso by mutableStateOf<WeightProgress?>(WeightProgress())
    var peso by mutableStateOf("")
    var pesoInvalido by mutableStateOf(false)

    var mostrarDialogoConfirmacion by mutableStateOf(false)
    var mostrarDialogoAgregarPeso by mutableStateOf(false)

    var isLoading by mutableStateOf(false)

    var filtroSeleccionado by mutableStateOf("Último mes")
        private set
    var datosFiltrados by mutableStateOf<List<Double>>(emptyList())
        private set
    private var ultimoPeso by mutableStateOf("")

    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    // Repositorios
    private val statisticsWeightRepository = StatisticsWeightRepository(auth, db)
    private val userRepository = UserRepository(auth, db)

    /**
     * Método para obtener el ultimo peso del usuario en base de datos
     */
    fun obtenerUltimoPeso(): String {
        val historial = obtenerHistorialOrdenado() ?: return "--"

        if (historial.isEmpty()) return "--"

        // Coge el ultimo peso disponible
        ultimoPeso = historial.last().first.peso.toString()
        return ultimoPeso
    }

    /**
     * Método que obtiene la diferencia de peso entre la ultima vez y la siguiente entrada
     */
    fun obtenerDiferenciaPeso(): String {
        // Convierte las fechas del historial a objetos LocalDate para ordenarlas
        val historial = progreso?.historial
            ?.mapNotNull {
                try {
                    it to LocalDate.parse(it.fecha, dateFormatter)
                } catch (e: Exception) {
                    null
                }
            }
            ?.sortedBy { it.second } // Las ordena
            ?: return "--"

        // Se necesitan minimo 2 entradas
        if (historial.size < 2) return "--"

        // Compara el peso actual con el anterior
        val pesoActual = historial.last().first.peso
        val pesoAnterior = historial[historial.size - 2].first.peso
        val diferencia = pesoActual - pesoAnterior

        // Define el signo (+, -, vacío)
        val signo = when {
            diferencia > 0 -> "+"
            diferencia < 0 -> "-"
            else -> ""
        }
        // Devuelve la diferencia con un decimal
        return "$signo%.1f kg".format(abs(diferencia))
    }


    /**
     * Método que obtiene el estado de color del peso
     */
    fun obtenerEstadoPeso(): Color {
        // Historial ordenado por fecha
        val historial = obtenerHistorialOrdenado()
            ?: return Color.Gray

        if (historial.size < 2) return Color.Gray

        val pesoActual = historial.last().first.peso
        val pesoAnterior = historial[historial.size - 2].first.peso

        // Si subió de peso: rojo, si bajó: verde
        return if (pesoActual > pesoAnterior) rojoBench else verdePrincipiante
    }

    /**
     * Método que obtiene el historial y lo ordena por fecha
     */
    private fun obtenerHistorialOrdenado(): List<Pair<StatisticsWeight, LocalDate>>? {
        // Convierte las fechas del historial y ordena por fecha
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


    /**
     * Método que selecciona el filtro a cargar
     */
    fun seleccionarFiltro(nuevoFiltro: String) {
        filtroSeleccionado = nuevoFiltro
        datosFiltrados = actualizarDatosFiltrados()
    }

    /**
     * Método que actualiza los datos filtrados determinando la fecha
     */
    private fun actualizarDatosFiltrados(): List<Double> {
        val historialCompleto = progreso?.historial ?: return emptyList()

        // Determina la fecha desde la cual filtrar
        val desde = when (filtroSeleccionado) {
            "Última semana" -> LocalDate.now().minusDays(7)
            "Último mes" -> LocalDate.now().minusMonths(1)
            "Último año" -> LocalDate.now().minusYears(1)
            else -> LocalDate.MIN
        }

        // Filtra y ordena las entradas por fecha
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
            .map { it.first.peso } // Devuelve solo los pesos
    }

    /**
     * Método que recupera el historial de pesos
     */
    suspend fun cargarHistorialPesos() {
        viewModelScope.launch {
            // Carga el historial del repositorio
            progreso = statisticsWeightRepository.getAllWeightProgress()
            // Actualiza los datos filtrados según el filtro seleccionado
            datosFiltrados = actualizarDatosFiltrados()
        }
    }

    /**
     * Método que abre el dialogo de agregar peso
     */
    fun abrirAgregarPeso() {
        mostrarDialogoAgregarPeso = true
    }

    /**
     * Método que cierra el dialogo de agregar peso
     */
    fun cerrarAgregarPeso() {
        mostrarDialogoAgregarPeso = false
        peso = ""
        pesoInvalido = false
    }

    /**
     * Método confirma la agregación de peso y la guarda en base de datos
     */
    fun confirmarAgregarPeso() {
        isLoading = true
        mostrarDialogoConfirmacion = false
        mostrarDialogoAgregarPeso = false

        viewModelScope.launch {
            try {
                val fechaHoy = LocalDate.now().format(dateFormatter)

                // Crea nueva entrada de peso
                val nuevoPeso = StatisticsWeight(
                    peso = peso.toDouble(),
                    fecha = fechaHoy
                )

                // Guarda el nuevo peso en Firestore
                statisticsWeightRepository.saveWeightExecution(newProgress = nuevoPeso)
                // También actualiza el peso del usuario
                userRepository.asignarPeso(peso = peso)
                // Recarga el historial
                cargarHistorialPesos()
            } finally {
                isLoading = false
                cerrarAgregarPeso()
            }
        }
    }
}
