package com.oscar.benchfitness.viewModels.datos

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.repository.FirebaseRepository
import com.oscar.benchfitness.utils.validateFieldsDatos

class DatosViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var altura by mutableStateOf("")
    var genero by mutableStateOf("Género")
    var peso by mutableStateOf("")
    var experiencia by mutableStateOf("Experiencia")
    var nivelActividad by mutableStateOf("Nivel de actividad física")
    var objetivo by mutableStateOf("Objetivo fitness")
    var birthday by mutableStateOf("")

    var isBirthdayChecked by mutableStateOf(false)  // Para controlar si ya se comprobó el birthday


    private val firebaseRepository = FirebaseRepository(auth, db)

    fun comprobarBirthdayUsuario() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { document->
                    // Verificar si el campo "birthday" existe y tiene valor
                    var birthdayDB = document.data?.get("birthday") as? String ?: ""
                    if (!birthdayDB.isNullOrEmpty()) {
                        birthday = birthdayDB // Si tiene un cumpleaños, actualizar el estado
                        isBirthdayChecked = true
                    }
                }
                .addOnFailureListener { e ->
                    // Manejar error en la consulta
                    birthday = ""
                }
        }
    }

    fun guardarDatosUsuario(context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        val (isValid, errorMessage) = validateFieldsDatos(
            altura = altura,
            genero = genero,
            nivelActividad = nivelActividad,
            objetivo = objetivo,
            peso = peso,
            experiencia = experiencia,
            birthday = birthday
        )

        if (!isValid) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            onFailure(errorMessage)
        } else {
            firebaseRepository.guardarDatosUsuario(
                userData(
                    altura = altura,
                    genero = genero,
                    peso = peso,
                    experiencia = experiencia,
                    nivelActividad = nivelActividad,
                    objetivo = objetivo,
                    birthday = birthday
                ),
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }
}