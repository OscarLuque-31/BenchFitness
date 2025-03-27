package com.oscar.benchfitness.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.navegation.Principal
import kotlinx.coroutines.tasks.await

class FirebaseRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    /**
     * Método para registrar al usuario en Firebase
     */
    suspend fun registerUser(
        email: String,
        password: String,
        username: String,
        birthday: String,
    ): Result<Unit> {
        return try {
            // Crea el usuario en firebase
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user =
                authResult.user ?: return Result.failure(Exception("Error al obtener usuario"))

            // Usuario a guardar en firebase
            val userData = userData(
                uid = user.uid,
                username = username,
                email = email,
                birthday = birthday,
            )

            db.collection("users").document(user.uid).set(userData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Método para guardar los datos del usuario en Firebase
     */
    fun guardarDatosUsuario(userData: userData, onSuccess: () -> Unit, onFailure: (String) -> Unit): Result<Unit> {
        val user = auth.currentUser
        return if (user != null) {
            // Guardamos todo el objeto `userData` en Firestore
            db.collection("users").document(user.uid)
                .update(
                    "altura", userData.altura,
                    "datosCompletados", true,
                    "experiencia", userData.experiencia,
                    "genero",userData.genero,
                    "nivelActividad",userData.nivelActividad,
                    "objetivo",userData.objetivo,
                    "peso",userData.peso,
                ).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { e ->
                    onFailure(e.message ?: "Error desconocido")
                }

            Result.success(Unit)
        } else {
            // Usuario no autenticado
            return Result.failure(Exception("Usuario no autenticado"))
        }
    }

    /**
     * Método para loguear al usuario en Firebase Auth
     */
    suspend fun loginUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun eliminarUsuario() {
        val user = auth.currentUser
        if (user != null) {
            val email = user.email ?: return
            val uid = user.uid

            // Buscar documentos en Firestore con el mismo email
            db.collection("users").whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("users").document(document.id).delete()
                    }

                    // Después de eliminar en Firestore, eliminar en FirebaseAuth
                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("EliminarUsuario", "Cuenta eliminada completamente")
                        } else {
                            Log.e("EliminarUsuario", "Error al eliminar la cuenta de Auth", task.exception)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("EliminarUsuario", "Error al eliminar el usuario de Firestore", e)
                }
        } else {
            Log.e("EliminarUsuario", "No hay usuario autenticado")
        }
    }
}



