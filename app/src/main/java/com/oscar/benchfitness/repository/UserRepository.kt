package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
import kotlinx.coroutines.tasks.await

class UserRepository(
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
            )

            // Registra al usuario en firebase
            db.collection("users").document(user.uid).set(userData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Método para guardar los datos del usuario en Firebase
     */
    fun guardarDatosUsuario(
        userData: userData,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ): Result<Unit> {
        val user = auth.currentUser
        // Si el usuario no es nulo se actualizan todos sus datos a los
        // introducidos en la pantalla de datos
        return if (user != null) {
            db.collection("users").document(user.uid)
                .update(
                    "altura", userData.altura,
                    "datosCompletados", true,
                    "experiencia", userData.experiencia,
                    "genero", userData.genero,
                    "nivelActividad", userData.nivelActividad,
                    "objetivo", userData.objetivo,
                    "peso", userData.peso,
                    "birthday", userData.birthday
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
            // Se loguea al usuario mediante email y contraseña
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    fun updateProfileUser(objetivo: String, altura: String) {
        val user = auth.currentUser ?: return

        val updates = mutableMapOf<String, Any>()

        if (objetivo.isNotBlank()) updates["objetivo"] = objetivo
        if (altura.isNotBlank()) updates["altura"] = altura

        if (updates.isNotEmpty()) {
            db.collection("users").document(user.uid).update(updates)
                .addOnFailureListener { it.printStackTrace() }
        }
    }



}



