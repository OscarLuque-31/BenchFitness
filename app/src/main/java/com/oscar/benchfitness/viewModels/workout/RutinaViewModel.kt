package com.oscar.benchfitness.viewModels.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.repository.RoutineRepository
import kotlinx.coroutines.launch

class RutinaViewModel(auth: FirebaseAuth, db: FirebaseFirestore) : ViewModel() {

    // Repositorio para manejar las rutinas
    private val routineRepository = RoutineRepository(auth, db)

    /**
     * Método para eliminar la rutina del usuario en firestore
     */
    fun eliminarRutina(nombre: String) {
        viewModelScope.launch {
            // Elimina la rutina
            routineRepository.eliminarRutina(nombre)
        }
    }
}