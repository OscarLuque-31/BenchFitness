package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
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

class HomeViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _userData = MutableStateFlow(userData())
    val userData: StateFlow<userData> = _userData.asStateFlow()

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
            )

            val userData = userData(
                username = nombre,
                objetivo = objetivo,
                peso = peso,
                altura = altura,
                nivelActividad = nivelActividad,
                genero = genero,
                birthday = birthday
            )

            // Se actualiza el hilo principal
            withContext(Dispatchers.Main) {
                _userData.value = userData
                _calorias.value = calorias
                _isLoading.value = false // Muestra la pantalla de carga

            }
        }
    }
}

