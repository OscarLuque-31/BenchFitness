package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.exercises.ExerciseData
import kotlinx.coroutines.tasks.await

class FavsRepository(auth: FirebaseAuth, db: FirebaseFirestore) {

    // Variable que representa la colección de ejercicios favs del usuario
    private val favoritesCollection = db.collection("users")
        .document(auth.currentUser!!.uid)
        .collection("favorites")

    /**
     * Método que obtiene todas las rutinas desde base de datos
     */
    suspend fun getAllFavs(): List<ExerciseData> {
        return try {
            val favorites = favoritesCollection.get().await()
            favorites.documents.mapNotNull { exercise ->
                try {
                    exercise.toObject(ExerciseData::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Método que cambia el ejercicio de a favorito y viceversa
     */
    suspend fun toogleFavorite(exerciseData: ExerciseData): Boolean {
        val documentRef = favoritesCollection.document(exerciseData.id_ejercicio)

        return try {
            val exists = documentRef.get().await().exists()

            if (exists) {
                documentRef.delete().await()
                false
            } else {
                documentRef.set(exerciseData).await()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Método que comprueba si el ejercicio es favorito
     */
    suspend fun isFavorite(exerciseId: String): Boolean {
        return try {
            favoritesCollection.document(exerciseId).get().await().exists()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
