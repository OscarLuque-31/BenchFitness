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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.Datos
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Main
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.validateLoginFields
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.concurrent.CancellationException


class LoginViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    // Variables necesarias para el login
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    // User repository para manejar al usuario en base de datos
    private val userRepository = UserRepository(auth, db)

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
                            // Si los datos están completados navega a la main
                            if (datosCompletos) {
                                navController.navigate(Main.route) {
                                    popUpTo(Inicio.route) {
                                        inclusive = true
                                    }
                                }
                                // Si no te redirige a que completes los datos
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
                    // Devuelve el mensaje de error correspondiente
                    val errorMsg = when {
                        it.message?.contains("The password is invalid") == true ||
                                it.message?.contains("no user record") == true -> {
                            "Correo o contraseña incorrectos. Verifica tus datos."
                        }

                        it.message?.contains("network error", ignoreCase = true) == true -> {
                            "Error de red. Verifica tu conexión a Internet."
                        }

                        it.message?.contains("too many requests", ignoreCase = true) == true -> {
                            "Demasiados intentos fallidos. Intenta de nuevo más tarde."
                        }

                        else -> {
                            "Error al iniciar sesión. Intenta nuevamente."
                        }
                    }
                    onFailure(errorMsg)
                }
            )
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
            try {
                loading = true

                // Configura el CredentialManager
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // Obtiene las credenciales del usuario
                val credentialResponse = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                val credential = credentialResponse.credential
                // Comprueba que las credenciales sean validas
                if (credential !is CustomCredential ||
                    credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    onFailure("Credenciales inválidas")
                    loading = false
                    return@launch
                }

                // Extrae el token de Google correctamente
                val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdToken.idToken ?: run {
                    onFailure("No se pudo obtener el token de Google")
                    loading = false
                    return@launch
                }

                // Decodifica el token JWT manualmente para obtener el email
                val payload = try {
                    val parts = idToken.split(".")
                    val payloadPart = parts[1]
                    val decodedBytes = android.util.Base64.decode(
                        payloadPart,
                        android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP
                    )
                    String(decodedBytes, Charsets.UTF_8)
                } catch (e: Exception) {
                    onFailure("Error al decodificar el token")
                    loading = false
                    return@launch
                }

                // Pasa el payload a un json object en el que poder leerse
                val jsonObject = try {
                    JSONObject(payload)
                } catch (e: Exception) {
                    onFailure("Error al parsear el token")
                    loading = false
                    return@launch
                }

                // Saca el email desde el json
                val email = jsonObject.optString("email").takeIf { it.isNotEmpty() } ?: run {
                    onFailure("No se encontró email en el token")
                    loading = false
                    return@launch
                }

                // Verifica los métodos de autenticación del usuario
                val signInMethods =
                    auth.fetchSignInMethodsForEmail(email).await().signInMethods ?: emptyList()

                // Comprueba si el email ya está o no registrado
                if (signInMethods.contains("password") && !signInMethods.contains("google.com")) {
                    onFailure("Este email ya está registrado con contraseña")
                    loading = false
                    return@launch
                }

                // Autentica con Firebase el el token del usuario
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)

                // Se loguea con la firebaseCredential
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                // Se guarda al usuario en firestore ademas de comprobar su existencia
                authResult.user?.let { user ->
                    val uid = user.uid
                    db.collection("users").whereEqualTo("email", email).get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                // Nuevo usuario si no existe
                                val userData = hashMapOf(
                                    "uid" to uid,
                                    "email" to email,
                                    "username" to (user.displayName?.split(" ")?.firstOrNull()
                                        ?: ""),
                                    "datosCompletados" to false
                                )
                                db.collection("users").document(uid).set(userData)
                                    .addOnSuccessListener { navController.navigate(Datos.route) }
                            } else {
                                // Si existe se comprueba el uid
                                val doc = documents.documents[0]
                                val needsUpdate = doc.get("uid") != uid

                                // Si necesita updatear el uid se hace si no se navega a a main o datos
                                // Segun si el usuario tiene o no completados los datos
                                if (needsUpdate) {
                                    doc.reference.update("uid", uid)
                                        .addOnSuccessListener {
                                            navigateBasedOnCompletion(
                                                doc,
                                                navController
                                            )
                                        }
                                } else {
                                    navigateBasedOnCompletion(doc, navController)
                                }
                            }
                        }
                }
            } catch (e: Exception) {
                onFailure(
                    when (e) {
                        is GetCredentialException -> "Error al obtener credenciales"
                        is CancellationException -> "Operación cancelada"
                        else -> "Error inesperado: ${e.localizedMessage}"
                    }
                )
            } finally {
                loading = false
            }
        }
    }

    /**
     * Método que redirige al usuario según tenga o no los datos completados
     */
    private fun navigateBasedOnCompletion(
        document: DocumentSnapshot,
        navController: NavController
    ) {
        val completed = document.getBoolean("datosCompletados") ?: false
        navController.navigate(if (completed) Main.route else Datos.route)
    }

    /**
     * Método que verifica si el usuario tiene o no los datos completados
     */
    private fun checkUserDataComplete(
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit = { _ -> }
    ) {
        // Comprueba si esta autenticado
        val currentUser = auth.currentUser ?: run {
            onFailure("No hay usuario autenticado")
            return
        }

        // Recoge al usuario desde base de datos para ver si existe
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
     * Método para limpiar el mensaje de error
     */
    fun clearError() {
        errorMessage = null
    }
}