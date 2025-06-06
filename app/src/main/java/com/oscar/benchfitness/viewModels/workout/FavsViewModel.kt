package com.oscar.benchfitness.viewModels.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.repository.FavsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavsViewModel(auth: FirebaseAuth, db: FirebaseFirestore) : ViewModel() {

    // Ejercicios favoritos
    private val _ejercicios = MutableStateFlow<List<ExerciseData>>(emptyList())
    val ejercicios: StateFlow<List<ExerciseData>> = _ejercicios

    // Repositorio para comprobar los favoritos
    private val favsRepository = FavsRepository(auth, db)

    /**
     * Método que carga los ejercicios favoritos del usuario
     */
    fun cargarEjerciciosFavs() {
        viewModelScope.launch {
            try {
                // Se obtienen los ejercicios favoritos
                _ejercicios.value = favsRepository.getAllFavs()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}