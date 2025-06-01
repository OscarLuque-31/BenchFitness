package com.oscar.benchfitness.viewModels.workout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.repository.FavsRepository
import kotlinx.coroutines.launch

class EjercicioViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var isFavorite by mutableStateOf(false)
        private set

    val favsRepository = FavsRepository(auth, db)

    fun toogleFavoriteUI(exerciseData: ExerciseData) {
        viewModelScope.launch {
            isFavorite = favsRepository.toogleFavorite(exerciseData)
        }
    }

    suspend fun checkIfFavoriteUI(exerciseId: String) {
        viewModelScope.launch {
            isFavorite = favsRepository.isFavorite(exerciseId)
        }
    }
}