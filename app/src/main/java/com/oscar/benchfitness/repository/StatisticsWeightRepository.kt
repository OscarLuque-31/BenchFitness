package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.StatisticsExercise
import com.oscar.benchfitness.models.statistics.StatisticsWeight
import com.oscar.benchfitness.models.statistics.WeightProgress
import kotlinx.coroutines.tasks.await

class StatisticsWeightRepository(auth: FirebaseAuth, db: FirebaseFirestore) {


    private val statisticsWeightCollection = db.collection("users")
        .document(auth.currentUser!!.uid)
        .collection("statisticsWeight")

    private val weightDocumentId = "Peso"

    // Guardar o actualizar una ejecución dentro del historial de un ejercicio
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

    suspend fun getAllWeightProgress(): WeightProgress? {
        val docSnapshot = statisticsWeightCollection.document(weightDocumentId).get().await()
        return if (docSnapshot.exists()) docSnapshot.toObject(WeightProgress::class.java) else null
    }


    suspend fun getLatestWeight(): StatisticsWeight? {
        val progress = getAllWeightProgress()
        return progress?.historial?.maxByOrNull { it.fecha }
    }



}