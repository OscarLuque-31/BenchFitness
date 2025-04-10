package com.oscar.benchfitness.viewModels.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.repository.FirebaseRepository
import com.oscar.benchfitness.utils.validateRegisterFields
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var acceptTerms by mutableStateOf(false)

    private val firebaseRepository = FirebaseRepository(auth, db)

    /**
     * Método que registra al usuario en base de datos
     */
    fun registerUser(onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        // Valida los datos
        val (isValid, errorMessage) = validateRegisterFields(
            username= username,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            acceptTerms = acceptTerms
        )

        // Si no es válida tira un mensaje de error
        if (!isValid) {
            onFailure(errorMessage)
            return
        }

        viewModelScope.launch {
            val result = firebaseRepository.registerUser(
                email = email,
                password = password,
                username = username,
            )

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure(it.message ?: "Error en el registro")}
            )
        }
    }
}