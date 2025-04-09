package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.ExerciseData
import kotlinx.coroutines.tasks.await

class EjercicioViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var isFavorite by mutableStateOf(false)
        private set

    fun toogleFavorite(exerciseData: ExerciseData) {
//        val userId = auth.currentUser?.uid ?: return
        isFavorite = !isFavorite

//        db.collection("users")
//            .document(userId)
//            .collection("favorites")
//            .document(exerciseData.id_ejercicio)
//            .apply {
//                if (isFavorite) {
//                    set(exerciseData).await()
//                } else {
//                    delete().await()
//                }
//            }
    }

    suspend fun checkIfFavorite(exerciseId: String) {
        val userId = auth.currentUser?.uid ?: return
        isFavorite = db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(exerciseId)
            .get()
            .await()
            .exists()
    }


}