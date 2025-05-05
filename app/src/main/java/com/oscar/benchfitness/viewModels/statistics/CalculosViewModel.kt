package com.oscar.benchfitness.viewModels.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.utils.CalorieCalculator

class CalculosViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var peso by mutableStateOf("")
    var altura by mutableStateOf("")
    var edad by mutableStateOf("")
    var nivelActividad by mutableStateOf("Nivel actividad física")
    var genero by mutableStateOf("Sexo")
    var calorias by mutableStateOf("")

    fun calcularCaloriasSuperavit() {
        calorias = CalorieCalculator().calcularCaloriasConObjetivo(
            "Masa muscular",
            peso = peso,
            altura = altura,
            edad = edad,
            nivelActividad = nivelActividad,
            genero = genero
        )
    }

    fun calcularCaloriasDeficit() {
        calorias = CalorieCalculator().calcularCaloriasConObjetivo(
            "Perder peso",
            peso = peso,
            altura = altura,
            edad = edad,
            nivelActividad = nivelActividad,
            genero = genero
        )
    }

    fun calcularCaloriasMantener() {
        calorias = CalorieCalculator().calcularCaloriasConObjetivo(
            "Mantener peso",
            peso = peso,
            altura = altura,
            edad = edad,
            nivelActividad = nivelActividad,
            genero = genero
        )
    }
}
