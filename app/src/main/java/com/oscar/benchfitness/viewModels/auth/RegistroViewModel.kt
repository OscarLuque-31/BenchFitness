
package com.oscar.benchfitness.viewModels.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.validateRegisterFields
import kotlinx.coroutines.launch

class RegistroViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    // Variables necesarias en la pantalla Registro
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var acceptTerms by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)

    // User repository para manejar al usuario en base de datos
    private val userRepository = UserRepository(auth, db)

    /**
     * Método que registra al usuario en base de datos
     */
    fun registerUser(onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        // Valida los datos del registro
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

        isLoading = true

        viewModelScope.launch {
            // Registra al usuario en base de datos con los datos mínimos
            val result = userRepository.registerUser(
                email = email,
                password = password,
                username = username,
            )
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure(it.message ?: "Error en el registro")}
            )
            isLoading = false
        }
    }

    /**
     * Método que limpia el mensaje de error
     */
    fun clearError() {
        errorMessage = null
    }
}