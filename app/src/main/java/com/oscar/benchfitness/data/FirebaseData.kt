package com.oscar.benchfitness.data

import android.util.Log
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.navegation.AppNavegation
import com.oscar.benchfitness.navegation.Principal
import com.oscar.benchfitness.screens.PrincipalScreen
import com.oscar.benchfitness.screens.validateFields

class FirebaseData {

    fun registerUser(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
        email: String,
        password: String,
        confirmPassword: String,
        username: String,
        birthdate: String,
        acceptTerms: Boolean,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val (isValid, errorMessage) = validateFields(
            username,
            email,
            password,
            confirmPassword,
            birthdate,
            acceptTerms
        )

        if (!isValid) {
            onFailure(errorMessage)
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val userData = hashMapOf(
                            "uid" to user.uid,
                            "username" to username,
                            "email" to email,
                            "birthdate" to birthdate
                        )

                        db.collection("users").document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d("Registro", "Usuario guardado en Firestore")
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                Log.e("Registro", "Error guardando usuario", e)
                                onFailure("Error guardando usuario en Firestore")
                            }
                    }
                } else {
                    task.exception?.let {
                        onFailure(it.message ?: "Error desconocido al registrar usuario")
                    }
                }
            }
    }

    fun guardarDatosUsuario(
        auth: FirebaseAuth,
        db: FirebaseFirestore,
        navController: NavController,
        datos: Map<String, Any>
    ) {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid)
                .update(datos)
                .addOnSuccessListener {
                    db.collection("users").document(user.uid)
                        .update("datosCompletados", true).addOnCompleteListener{
                            navController.navigate(Principal)
                        }
                }
                .addOnFailureListener {  }
        }
    }
}



