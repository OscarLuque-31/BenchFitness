package com.oscar.benchfitness.viewModels.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.repository.RoutineRepository
import com.oscar.benchfitness.viewModels.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PerfilViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    var usuario by mutableStateOf(userData())

    var numRutinas by mutableStateOf("0")

    var isLoading by mutableStateOf(true)

    val routineRepository = RoutineRepository(auth, db)

    val isGoogleUser = auth.currentUser?.providerData?.any { it.providerId == "google.com" } == true

    fun cargarPerfilUsuario() {
        isLoading = true
        val user = auth.currentUser ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val document = db.collection("users").document(user.uid).get().await()
            val nombre = document.getString("username") ?: ""
            val objetivo = document.getString("objetivo") ?: ""
            val email = document.getString("email") ?: ""
            val altura = document.getString("altura") ?: ""
            val birthday = document.getString("birthday") ?: ""
            numRutinas = routineRepository.getNumberOfRoutines()

            var userData = userData(
                username = nombre,
                objetivo = objetivo,
                email = email,
                birthday = birthday,
                altura = altura
            )

            // Se actualiza el hilo principal
            withContext(Dispatchers.Main) {
                usuario = userData
            }
            isLoading = false
        }
    }


    fun cerrarSesion() {
            authViewModel.cerrarSesion()
    }
}