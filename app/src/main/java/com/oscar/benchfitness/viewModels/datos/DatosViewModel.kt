package com.oscar.benchfitness.viewModels.datos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.models.statistics.StatisticsWeight
import com.oscar.benchfitness.models.user.userData
import com.oscar.benchfitness.repository.RoutineRepository
import com.oscar.benchfitness.repository.StatisticsWeightRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.crearRutinaAvanzado
import com.oscar.benchfitness.utils.crearRutinaIntermedio
import com.oscar.benchfitness.utils.crearRutinaPrincipiante
import com.oscar.benchfitness.utils.validateFieldsDatos
import kotlinx.coroutines.launch

class DatosViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    // Variables necesarias para los datos
    var altura by mutableStateOf("")
    var genero by mutableStateOf("Sexo")
    var peso by mutableStateOf("")
    var experiencia by mutableStateOf("Experiencia")
    var nivelActividad by mutableStateOf("Nivel de actividad física")
    var objetivo by mutableStateOf("Objetivo fitness")
    var birthday by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var snackbarMessage by mutableStateOf<String?>(null)
    private var rutinaPorDefecto by mutableStateOf(Routine())

    // Repositorios para guardar los datos en base de datos
    private val userRepository = UserRepository(auth, db)
    private val statisticsWeightRepository = StatisticsWeightRepository(auth, db)
    private val routineRepository = RoutineRepository(auth, db)

    /**
     * Método que guarda los datos del usuario en base de datos
     */
    fun guardarDatosUsuario(onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        // Valida que sean correctos
        val (isValid, errorMessage) = validateFieldsDatos(
            altura = altura,
            genero = genero,
            nivelActividad = nivelActividad,
            objetivo = objetivo,
            peso = peso,
            experiencia = experiencia,
            birthday = birthday
        )

        // Si no son válidos lanza un mensaje de error, sino guarda los datos
        if (!isValid) {
            snackbarMessage = errorMessage
            onFailure(errorMessage)
        } else {
            isLoading = true
            // Guarda los datos del usuario en base de datos
            userRepository.guardarDatosUsuario(
                userData(
                    altura = altura,
                    genero = genero,
                    peso = peso,
                    experiencia = experiencia,
                    nivelActividad = nivelActividad,
                    objetivo = objetivo,
                    birthday = birthday
                ),
                onSuccess = {
                    isLoading = false
                    onSuccess()
                },
                onFailure = { error ->
                    isLoading = false
                    onFailure(error)
                }
            )
            viewModelScope.launch {
                // Asigna el primer peso del usuario a su progreso de peso
                statisticsWeightRepository.saveWeightExecution(newProgress = StatisticsWeight(peso = peso.toDouble()))
                // Asigna una rutina por defecto según su experiencia
                asignarRutinaSegunExperiencia(experiencia)
            }
        }
    }

    /**
     * Método que asigna una rutina al usuario según su experiencia
     */
    private suspend fun asignarRutinaSegunExperiencia(experiencia: String) {
        when (experiencia) {
            "Principiante" -> rutinaPorDefecto = crearRutinaPrincipiante()
            "Intermedio" -> rutinaPorDefecto = crearRutinaIntermedio()
            "Avanzado" -> rutinaPorDefecto = crearRutinaAvanzado()
        }
        // Guarda la rutina al usuario
        routineRepository.saveRoutine(rutinaPorDefecto)
    }

    /**
     * Método que limpia el snackbar
     */
    fun clearSnackbar() {
        snackbarMessage = null
    }
}