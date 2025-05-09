package com.oscar.benchfitness.viewModels.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.Datos
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Main
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.validateLoginFields
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private val userRepository = UserRepository(auth, db)



    fun clearError() {
        errorMessage = null
    }

    /**
     * Método que loguea al usuario con email y contraseña
     */
    fun loginUser(
        navController: NavController,
        onFailure: (String) -> Unit
    ) {
        // Comprueba si es válido
        val (isValid, errorMessage) = validateLoginFields(email, password)
        // Si no lo es muestra un mensaje de error
        if (!isValid) {
            onFailure(errorMessage)
            return
        }

        loading = true
        viewModelScope.launch {
            // Trata de loguear al usuario
            val result = userRepository.loginUser(email, password)

            result.fold(
                onSuccess = {
                    // Verificar estado de los datos
                    checkUserDataComplete(
                        onSuccess = { datosCompletos ->
                            loading = false
                            if (datosCompletos) {
                                navController.navigate(Main.route) {
                                    popUpTo(Inicio.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                navController.navigate(Datos.route) {
                                    popUpTo(Inicio.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        },
                        onFailure = { error ->
                            loading = false
                            onFailure("Error al verificar datos: $error")
                        }
                    )
                },
                onFailure = {
                    loading = false
                    onFailure(it.message ?: "Error en el inicio de sesión")
                }
            )
        }
    }

    /**
     * Método que verifica si el usuario tiene o no los datos completados
     */
    private fun checkUserDataComplete(
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit = { _ -> }
    ) {
        val currentUser = auth.currentUser ?: run {
            onFailure("No hay usuario autenticado")
            return
        }

        // Recoge al usuario
        db.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    onFailure("El documento del usuario no existe")
                    return@addOnSuccessListener
                }

                try {
                    val camposRequeridos = listOf(
                        "altura", "peso", "genero", "birthday",
                        "nivelActividad", "objetivo", "experiencia"
                    )

                    // Si todos los campos estan completados marcará true
                    val datosCompletos = camposRequeridos.all { field ->
                        document.getString(field)?.isNotEmpty() == true
                    }

                    onSuccess(datosCompletos)
                } catch (e: Exception) {
                    onFailure("Error al verificar datos: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                onFailure("Error al acceder a Firestore: ${e.message}")
            }
    }

    /**
     * Método que loguea al usuario con google
     */
    fun loginWithGoogle(
        context: Context,
        navController: NavController,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Crea el manejador de credenciales
            val credentialManager: CredentialManager = CredentialManager.create(context)

            try {
                loading = true

                // Crea el dialogo para seleccionar las cuentas
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                // Crea la request con la opción seleccionada
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Obtiene la credencial de la request anterior
                val credentialResponse = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = credentialResponse.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                    val firebaseCredential = GoogleAuthProvider.getCredential(
                        googleIdToken.idToken,
                        null
                    )

                    // Inicia sesión con la credencial
                    val authResult = auth.signInWithCredential(firebaseCredential).await()
                    authResult.user?.let { firebaseUser ->
                        val email = firebaseUser.email ?: ""
                        val uid = firebaseUser.uid

                        if (email.isNotEmpty()) {
                            // Verifica si el usuario ya existe en Firestore
                            db.collection("users").whereEqualTo("email", email).get()
                                .addOnSuccessListener { documents ->
                                    if (!documents.isEmpty) {
                                        // Si el usuario ya existe se actualiza el UID en Firestore
                                        for (document in documents) {
                                            db.collection("users").document(document.id)
                                                .update("uid", uid)
                                                .addOnSuccessListener {

                                                    val datosCompletados =
                                                        document.getBoolean("datosCompletados")
                                                            ?: false

                                                    if (datosCompletados) {
                                                        // Si los datos están completos, navega a la pantalla principal
                                                        navController.navigate(Main.route)
                                                    } else {
                                                        // Si los datos no están completos, navega a la pantalla para completar los datos
                                                        navController.navigate(Datos.route)
                                                    }

                                                }
                                                .addOnFailureListener { e ->
                                                    onFailure("Error al actualizar UID: ${e.message}")
                                                }
                                        }
                                    } else {
                                        // Si no existe se guarda el usuario en Firestore
                                        val userData = hashMapOf(
                                            "uid" to uid,
                                            "username" to (firebaseUser.displayName?.split(" ")
                                                ?.firstOrNull() ?: ""),
                                            "email" to email
                                        )

                                        db.collection("users").document(uid)
                                            .set(userData)
                                            .addOnSuccessListener {
                                                navController.navigate(Datos.route)
                                            }
                                            .addOnFailureListener { e ->
                                                onFailure("Error al guardar datos del usuario: ${e.message}")
                                            }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    onFailure("Error al verificar usuario en Firestore: ${e.message}")
                                }
                        } else {
                            onFailure("No se pudo obtener el correo electrónico del usuario.")
                        }
                    }
                } else {
                    onFailure("Credencial no válida")
                }
            } catch (e: GetCredentialException) {
                onFailure("Error al obtener credenciales: ${e.message}")
            } catch (e: Exception) {
                onFailure("Error inesperado: ${e.message}")
            } finally {
                loading = false
            }
        }
    }
}