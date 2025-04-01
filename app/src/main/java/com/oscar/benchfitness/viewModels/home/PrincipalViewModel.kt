package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.utils.CalorieCalculator
import com.oscar.benchfitness.utils.calcularEdad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class PrincipalViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {


    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _objetivo = MutableStateFlow("")
    val objetivo = _objetivo.asStateFlow()

    private val _calorias = MutableStateFlow("")
    val calorias = _calorias.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun cargarDatosUsuario() {
        _isLoading.value = true // Muestra la pantalla de carga

        val user = auth.currentUser ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val document = db.collection("users").document(user.uid).get().await()
            val nombre = document.getString("username") ?: ""
            val objetivo = document.getString("objetivo") ?: ""
            val peso = document.getString("peso") ?: ""
            val altura = document.getString("altura") ?: ""
            val nivelActividad = document.getString("nivelActividad") ?: ""
            val genero = document.getString("genero") ?: ""
            val birthday = document.getString("birthday") ?: ""

            val calorias = CalorieCalculator().calcularCaloriasConObjetivo(
                objetivo = objetivo,
                altura = altura,
                peso = peso,
                nivelActividad = nivelActividad,
                genero = genero,
                edad = calcularEdad(birthday)
            ).split(".").first()

            // Se actualiza el hilo principal
            withContext(Dispatchers.Main) {
                _nombre.value = nombre
                _objetivo.value = objetivo
                _calorias.value = calorias
                _isLoading.value = false // Muestra la pantalla de carga

            }
        }
    }



    fun interpretarObjetivo(objetivo: String): String {
        return when (objetivo) {
            "Perder peso" -> "Déficit"
            "Mantener peso" -> "Mantener"
            "Masa muscular" -> "Superávit"
            else -> "Desconocido"
        }
    }

}

