package com.oscar.benchfitness.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.models.user.userData
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val userCollection = db.collection("users")

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
            userCollection.document(user.uid).set(userData).await()

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
            userCollection.document(user.uid)
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

    /**
     * Método para actualizar el perfil del usuario
     */
    fun updateProfileUser(objetivo: String, altura: String) {
        val user = auth.currentUser ?: return

        val updates = mutableMapOf<String, Any>()

        if (objetivo.isNotBlank()) updates["objetivo"] = objetivo
        if (altura.isNotBlank()) updates["altura"] = altura

        if (updates.isNotEmpty()) {
            userCollection.document(user.uid).update(updates)
                .addOnFailureListener { it.printStackTrace() }
        }
    }

    /**
     * Método para asignar la rutina al usuario
     */
    suspend fun asignarRutina(rutina: Routine) {
        val user = auth.currentUser ?: return

        userCollection.document(user.uid).update("rutinaAsignada", rutina).await()
        userCollection.document(user.uid).update("isRutinaAsignada", true).await()
    }

    /**
     * Método para comprobar si hay alguna rutina asignada
     */
    suspend fun isRutinaAsignada(): Boolean {
        val user = auth.currentUser ?: return false

        val document = userCollection.document(user.uid).get().await()

        val rutinaAsignada = document.getBoolean("isRutinaAsignada") ?: false

        return rutinaAsignada
    }

    /**
     * Método para obtener toda la información del usuario
     */
    suspend fun getUserInformation(): userData {
        val user = auth.currentUser ?: return userData()
        return try {
            val userInfo = userCollection.document(user.uid).get().await()
            return userInfo.toObject(userData::class.java) ?: return userData()
        } catch (e: Exception) {
            e.printStackTrace()
            userData()
        }
    }

    /**
     * Método para desasignar la rutina del usuario
     */
    suspend fun desasignarRutina() {
        val user = auth.currentUser ?: return
        userCollection.document(user.uid)
            .update(
                mapOf(
                    "rutinaAsignada" to null,
                    "isRutinaAsignada" to false
                )
            ).await()
    }

    /**
     * Método para asignar el peso al usuario
     */
    suspend fun asignarPeso(peso: String) {
        val user = auth.currentUser ?: return

        userCollection.document(user.uid).update("peso", peso).await()
    }
}



