package com.oscar.benchfitness.viewModels.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.user.userData
import com.oscar.benchfitness.repository.RoutineRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.validatePassword
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

    // Variables necesarias para el perfil
    var usuario by mutableStateOf(userData())
    val isGoogleUser = auth.currentUser?.providerData?.any { it.providerId == "google.com" } == true

    var numRutinas by mutableStateOf("0")

    var editAltura by mutableStateOf(false)
    var editObjetivo by mutableStateOf(false)
    var newObjetivo by mutableStateOf(usuario.objetivo)
    var newAltura by mutableStateOf(usuario.altura)

    var showLogoutDialog by mutableStateOf(false)
    var showPasswordDialog by mutableStateOf(false)
    var showSuccessDialog by mutableStateOf(false)

    var passwordError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(true)

    // Repositorios para guardar y recoger la información de base de datos
    private val routineRepository = RoutineRepository(auth, db)
    private val userRepository = UserRepository(auth, db)


    /**
     * Método que carga los datos necesarios para completar el perfil del usuario
     */
    fun cargarPerfilUsuario() {
        isLoading = true
        viewModelScope.launch {
            // Recoge la información del usuario
            usuario = userRepository.getUserInformation()
            // Recoge la cantidad de rutinas del usuario
            numRutinas = routineRepository.getNumberOfRoutines()
            isLoading = false
        }
    }

    /**
     * Método que intenta cambiar la password si es válida
     */
    fun intentarCambiarPassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        passwordError = null // Resetear error

        // Valida si la contraseña es valida
        val (isValid, errorMsg) = validatePassword(newPassword)
        when {
            newPassword != confirmPassword -> passwordError = "Las contraseñas no coinciden"
            !isValid -> passwordError = errorMsg
            // Si no hay ningun error la cambia
            else -> cambiarPassword(currentPassword, newPassword)
        }
    }

    /**
     * Método que cambia la contraseña definitivamente en Firebase Auth
     */
    private fun cambiarPassword(
        currentPassword: String,
        newPassword: String
    ) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            viewModelScope.launch {
                try {
                    // Comprueba el usuario con su credencial
                    user.reauthenticate(credential).await()
                    // Cambia la contraseña al usuario
                    user.updatePassword(newPassword).await()

                    showPasswordDialog = false // Cierra el diálogo
                    showSuccessDialog = true

                } catch (e: Exception) {
                    passwordError = "Error al cambiar la contraseña: ${e.localizedMessage}"
                }
            }
        } else {
            passwordError = "No se pudo obtener el usuario actual"
        }
    }

    /**
     * Método parar la sesión del usuario
     */
    fun cerrarSesion() {
        authViewModel.cerrarSesion()
    }

    /**
     * Método que guarda los cambios hechos en el perfil
     */
    fun guardarCambios() {
        viewModelScope.launch {
            // Actualiza el perfil con su nuevo Objetivo y Altura
            userRepository.updateProfileUser(newObjetivo, newAltura)
            editObjetivo = false
            editAltura = false
            // Carga de nuevo el perfil del usuario
            cargarPerfilUsuario()
        }
    }
}
