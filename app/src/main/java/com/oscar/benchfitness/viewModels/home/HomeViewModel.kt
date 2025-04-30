package com.oscar.benchfitness.viewModels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.utils.CalorieCalculator
import com.oscar.benchfitness.utils.calcularEdad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
        _isLoading.value = true
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

            val userData = userData(
                username = nombre,
                objetivo = objetivo,
                peso = peso,
                altura = altura,
                nivelActividad = nivelActividad,
                genero = genero,
                birthday = birthday
            )

            val caloriasCalculadas = calcularDatos(userData)

            // Se actualiza el hilo principal
            withContext(Dispatchers.Main) {
                _userData.value = userData
                _isLoading.value = false
                _calorias.value = caloriasCalculadas

            }
        }
    }

    private fun calcularDatos(user: userData): String {
        return CalorieCalculator().calcularCaloriasConObjetivo(
            objetivo = user.objetivo,
            altura = user.altura,
            peso = user.peso,
            nivelActividad = user.nivelActividad,
            genero = user.genero,
            edad = calcularEdad(user.birthday)
        )
    }
}


