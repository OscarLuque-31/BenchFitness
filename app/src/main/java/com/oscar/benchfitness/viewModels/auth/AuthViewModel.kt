package com.oscar.benchfitness.viewModels.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var isAuthenticated by mutableStateOf(auth.currentUser != null)
        private set

    var datosCompletados by mutableStateOf(false)
        private set

    fun verificarEstado() {
        val currentUser = auth.currentUser
        isAuthenticated = currentUser != null

        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    datosCompletados = document.getBoolean("datosCompletados") == true
                }
                .addOnFailureListener {
                    datosCompletados = false
                }
        } else {
            datosCompletados = false
        }
    }

    fun cerrarSesion() {
        auth.signOut()
        isAuthenticated = false
        datosCompletados = false
    }
}
