package com.oscar.benchfitness.viewModels.exercises

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EjerciciosViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var musculo by mutableStateOf("Músculo")
    var categoria by mutableStateOf("Categoría")
    var nivel by mutableStateOf("Nivel")
    var busqueda by mutableStateOf("")

    var opcionesMusculo by mutableStateOf(listOf("fsadf","fsdf"))
    var opcionesCategoria by mutableStateOf(listOf("fsadf","fsdf"))
    var opcionesNivel by mutableStateOf(listOf("fsadf","fsdf"))




}