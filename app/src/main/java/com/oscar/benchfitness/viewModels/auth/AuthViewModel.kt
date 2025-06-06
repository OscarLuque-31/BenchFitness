package com.oscar.benchfitness.viewModels.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    // Variable para ver si el usuario esta autenticado
    var isAuthenticated by mutableStateOf(auth.currentUser != null)
        private set

    var datosCompletados by mutableStateOf(false)
        private set

    /**
     * Método que verifica el estado del usuario
     */
    fun verificarEstado() {
        val currentUser = auth.currentUser
        isAuthenticated = currentUser != null

        if (currentUser != null) {
            // Comprueba si existe en base de datos
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    // Comprueba que los datos esten completados
                    datosCompletados = document.getBoolean("datosCompletados") == true
                }
                .addOnFailureListener {
                    datosCompletados = false
                }
        } else {
            datosCompletados = false
        }
    }

    /**
     * Método que cierra la sesión del usuario
     */
    fun cerrarSesion() {
        auth.signOut()
        isAuthenticated = false
        datosCompletados = false
    }
}
