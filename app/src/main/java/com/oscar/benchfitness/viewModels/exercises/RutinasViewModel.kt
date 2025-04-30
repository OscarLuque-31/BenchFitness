package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.repository.RoutineRepository
import kotlinx.coroutines.launch

class RutinasViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var listaRutinas by mutableStateOf<List<Routine>>(emptyList())

    private val routineRepository = RoutineRepository(auth,db)

    fun obtenerRutinas() {
        viewModelScope.launch {
            try {
                val rutinas = routineRepository.getAllRoutines()
                listaRutinas = rutinas
            } catch (e: Exception) {
                // Manejo de errores (puedes mostrar un mensaje de error si es necesario)
                println("Error al obtener los ejercicios: ${e.message}")
            }
        }
    }
}