package com.oscar.benchfitness.viewModels.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.repository.RoutineRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.viewModels.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers
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

    var editAltura by mutableStateOf(false)
    var editObjetivo by mutableStateOf(false)
    var newObjetivo by mutableStateOf(usuario.objetivo)
    var newAltura by mutableStateOf(usuario.altura)

    var isLoading by mutableStateOf(true)

    private val routineRepository = RoutineRepository(auth, db)
    private val userRepository = UserRepository(auth, db)

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

    fun validarObjetivo(): String? {
        return if (newObjetivo.isBlank()) "Por favor selecciona un objetivo válido." else null
    }

    fun validarAltura(): String? {
        val alturaInt = newAltura.toIntOrNull()
        return if (alturaInt == null || alturaInt !in 80..250)
            "Introduce una altura válida entre 80 y 250 cm."
        else null
    }

    fun guardarCambios() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateProfileUser(newObjetivo, newAltura)
            withContext(Dispatchers.Main) {
                editObjetivo = false
                editAltura = false
                cargarPerfilUsuario()
            }
        }
    }


}