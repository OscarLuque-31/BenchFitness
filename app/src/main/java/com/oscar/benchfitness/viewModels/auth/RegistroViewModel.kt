package com.oscar.benchfitness.viewModels.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.repository.FirebaseRepository
import com.oscar.benchfitness.utils.validateFields
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var username = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var birthday = mutableStateOf("")
    var acceptTerms = mutableStateOf(false)

    private val firebaseRepository = FirebaseRepository(auth, db)

    fun registerUser(onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        val (isValid, errorMessage) = validateFields(
            username.value,
            email.value,
            password.value,
            confirmPassword.value,
            birthday.value,
            acceptTerms.value
        )

        if (!isValid) {
            onFailure(errorMessage)
            return
        }

        viewModelScope.launch {
            val result = firebaseRepository.registerUser(
                email.value,
                password.value,
                confirmPassword.value,
                username.value,
            )

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure(it.message ?: "Error en el registro")}
            )
        }
    }
}