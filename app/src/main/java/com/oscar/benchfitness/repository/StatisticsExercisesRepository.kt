package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.StatisticsExercise
import kotlinx.coroutines.tasks.await

class StatisticsExercisesRepository(auth: FirebaseAuth, db: FirebaseFirestore) {

    private val statisticsExercisesCollection = db.collection("users")
        .document(auth.currentUser!!.uid)
        .collection("statisticsExercises")

    // Guardar o actualizar una ejecución dentro del historial de un ejercicio
    suspend fun saveOrUpdateExerciseExecution(exerciseName: String, newProgress: StatisticsExercise) {
        val existingDoc = statisticsExercisesCollection.document(exerciseName).get().await()
        val existingProgress = if (existingDoc.exists()) {
            existingDoc.toObject(ExerciseProgress::class.java)
        } else {
            ExerciseProgress(nombre = exerciseName)
        }

        // Reemplaza la ejecución si ya existe una con la misma fecha
        val updatedHistorial = existingProgress!!.historial
            .filterNot { it.fecha == newProgress.fecha } + newProgress

        val updatedExercise = existingProgress.copy(historial = updatedHistorial)
        statisticsExercisesCollection.document(exerciseName).set(updatedExercise).await()
    }

    // Obtener progreso de un ejercicio por nombre
    suspend fun getExerciseProgressByName(exerciseName: String): ExerciseProgress? {
        val docSnapshot = statisticsExercisesCollection.document(exerciseName).get().await()
        return if (docSnapshot.exists()) docSnapshot.toObject(ExerciseProgress::class.java) else null
    }

    // Obtener todas las ejecuciones registradas
    suspend fun getAllExerciseProgress(): List<ExerciseProgress> {
        val snapshot = statisticsExercisesCollection.get().await()
        return snapshot.toObjects(ExerciseProgress::class.java)
    }

}