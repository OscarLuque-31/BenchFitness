package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.repository.RoutineRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.CalorieCalculator
import com.oscar.benchfitness.utils.calcularEdad
import kotlinx.coroutines.launch

class HomeViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var userData by mutableStateOf(userData())
    var calorias by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var rutinaSeleccionada by mutableStateOf(Routine())
    var rutinas by mutableStateOf(emptyList<Routine>())
    var isRutinaAsignada by mutableStateOf(false)
    var cambiarRutina by mutableStateOf(false)
    var isRoutineLoading by mutableStateOf(false)


    private val routineRepository = RoutineRepository(auth, db)
    private val userRepository = UserRepository(auth, db)

    suspend fun cargarDatosUsuario() {
        isLoading = true
        userData = userRepository.getUserInformation()
        rutinas = routineRepository.getAllRoutines()
        calorias = calcularDatos(userData)
        isLoading = false
    }


    private fun calcularDatos(user: userData): String {
        return CalorieCalculator().calcularCaloriasConObjetivo(
            objetivo = user.objetivo,
            altura = user.altura,
            peso = user.peso,
            nivelActividad = user.nivelActividad,
            genero = user.genero,
            edad = calcularEdad(user.birthday)
        )
    }

    fun asignarRutina() {
        viewModelScope.launch {
            isRoutineLoading = true
            userRepository.asignarRutina(rutinaSeleccionada)
            userData = userRepository.getUserInformation()
            isRutinaAsignada = true
            isRoutineLoading = false
        }
    }

    fun comprobarRutinaAsignada() {
        viewModelScope.launch {
            isRutinaAsignada = userRepository.isRutinaAsignada()
        }
    }

    fun desasignarRutina() {
        viewModelScope.launch {
            userRepository.desasignarRutina()
            userData = userRepository.getUserInformation()
            rutinaSeleccionada = Routine()
            isRutinaAsignada = false
        }
    }
}




