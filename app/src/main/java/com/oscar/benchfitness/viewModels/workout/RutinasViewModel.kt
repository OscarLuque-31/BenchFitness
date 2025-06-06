package com.oscar.benchfitness.viewModels.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.repository.RoutineRepository
import kotlinx.coroutines.launch

class RutinasViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var listaRutinas by mutableStateOf<List<Routine>>(emptyList())

    // Repositorio para obtener las rutinas
    private val routineRepository = RoutineRepository(auth, db)

    /**
     * Método que obtiene todas las rutinas del usuario
     */
    fun obtenerRutinas() {
        viewModelScope.launch {
            try {
                // Obtiene las rutinas
                val rutinas = routineRepository.getAllRoutines()
                listaRutinas = rutinas
            } catch (e: Exception) {
                println("Error al obtener los ejercicios: ${e.message}")
            }
        }
    }
}