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

    var isLoading by mutableStateOf(false)


    private val firebaseRepository = FirebaseRepository(auth, db)

    fun guardarDatosUsuario(context: Context, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        isLoading = true

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
                onSuccess = {
                    isLoading = false
                    onSuccess()},
                onFailure = { error ->
                    isLoading = false
                    onFailure(error)
                }
            )
        }
    }
}