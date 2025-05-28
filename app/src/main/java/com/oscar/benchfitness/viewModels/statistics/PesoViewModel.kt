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


    var pesoInvalido by mutableStateOf(false)

    private val statisticsWeightRepository = StatisticsWeightRepository(auth, db)
    private val userRepository = UserRepository(auth, db)



    // Función simplificada para obtener diferencia
    fun obtenerDiferenciaPeso(): String {
        val historial = progreso?.historial?.sortedBy { it.fecha } ?: return "--"
        if (historial.size < 2) return "--"

        val diferencia = historial.last().peso - historial[historial.size - 2].peso
        return "%.1f kg".format(diferencia)
    }

    // Función para saber si aumentó o bajó
    fun obtenerEstadoPeso(): Color {
        val historial = progreso?.historial?.sortedBy { it.fecha } ?: return Color.Gray
        if (historial.size < 2) return Color.Gray

        val diferencia = historial.last().peso - historial[historial.size - 2].peso
        return if (diferencia > 0) rojoBench else verdePrincipiante
    }

    fun cargarHistorialPesos(){
        viewModelScope.launch {
            progreso = statisticsWeightRepository.getAllWeightProgress()
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