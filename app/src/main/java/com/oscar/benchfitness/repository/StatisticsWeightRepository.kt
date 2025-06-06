package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.StatisticsWeight
import com.oscar.benchfitness.models.statistics.WeightProgress
import kotlinx.coroutines.tasks.await

class StatisticsWeightRepository(auth: FirebaseAuth, db: FirebaseFirestore) {

    // Variable que representa la colección de estadisticas de peso del usuario
    private val statisticsWeightCollection = db.collection("users")
        .document(auth.currentUser!!.uid)
        .collection("statisticsWeight")

    // Id del documento
    private val weightDocumentId = "Peso"

    /**
     * Guarda o actualiza una ejecución dentro del historial de un ejercicio
     */
    suspend fun saveWeightExecution(newProgress: StatisticsWeight) {
        val existingDoc = statisticsWeightCollection.document(weightDocumentId).get().await()
        val existingProgress = if (existingDoc.exists()) {
            existingDoc.toObject(WeightProgress::class.java)
        } else {
            WeightProgress(historial = emptyList())
        }

        // Reemplaza la ejecución si ya existe una con la misma fecha
        val updatedHistorial = (existingProgress!!.historial
            .filterNot { it.fecha == newProgress.fecha } + newProgress)
            .sortedBy { it.fecha }

        val updatedExercise = existingProgress.copy(historial = updatedHistorial)
        statisticsWeightCollection.document(weightDocumentId).set(updatedExercise).await()
    }

    /**
     * Obtiene todas las entradas de peso del usuario
     */
    suspend fun getAllWeightProgress(): WeightProgress? {
        val docSnapshot = statisticsWeightCollection.document(weightDocumentId).get().await()
        return if (docSnapshot.exists()) docSnapshot.toObject(WeightProgress::class.java) else null
    }
}