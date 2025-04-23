package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.Routine
import kotlinx.coroutines.tasks.await


class RoutineRepository (auth: FirebaseAuth, db: FirebaseFirestore) {

    private val routinesCollection = db.collection("users")
            .document(auth.currentUser!!.uid)  // !! porque confiamos en la autenticación
            .collection("routines")
    /**
     * Método para guardar los datos de la rutina creada por el usuario
     */
    suspend fun saveRoutine(routine: Routine): String {
        return try {
            routinesCollection.add(routine.toMap()).await().id
        } catch (e: Exception) {
            throw e
        }
    }
}