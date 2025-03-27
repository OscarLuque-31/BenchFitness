package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrincipalViewModel : ViewModel() {

    var nombre by mutableStateOf("")
    var calorias by mutableStateOf("")

    fun cargarNombreUsuario(auth: FirebaseAuth, db: FirebaseFirestore) {
        val user = auth.currentUser
        user?.let {
            db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
                nombre = document.getString("username").toString()
            }
        }
    }
}

