package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.Routine
import kotlinx.coroutines.tasks.await


class RoutineRepository(auth: FirebaseAuth, db: FirebaseFirestore) {

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

    /**
     * Método que obtiene todas las rutinas desde base de datos
     */
    suspend fun getAllRoutines(): List<Routine> {
        return try {
            val rutinas = routinesCollection.get().await()
            rutinas.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Routine::class.java)
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
     * Método que obtiene el número de rutinas creadas
     */
    suspend fun getNumberOfRoutines(): String {
        return try {
            val rutinas = routinesCollection.get().await()
            rutinas.documents.size.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            "0"
        }
    }

    /**
     * Método que elimina la rutina por nombre elegida por el usuario
     */
    suspend fun eliminarRutina(nombre: String): Boolean {
        return try {
            val rutinas = routinesCollection.whereEqualTo("nombre", nombre).get().await()

            // Si no hay ninguna rutina con ese nombre se devuelve false
            if (rutinas.isEmpty) {
                return false
            }

            for (rutina in rutinas) {
                rutina.reference.delete().await()
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
