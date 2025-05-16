package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.saveable.autoSaver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.repository.RoutineRepository
import kotlinx.coroutines.launch

class RutinaViewModel(auth: FirebaseAuth, db: FirebaseFirestore) : ViewModel() {


    private val routineRepository = RoutineRepository(auth,db)


    fun eliminarRutina(nombre: String) {
        viewModelScope.launch {
            routineRepository.eliminarRutina(nombre)
        }
    }
}