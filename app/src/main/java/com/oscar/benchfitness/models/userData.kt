package com.oscar.benchfitness.models

data class userData(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val birthday: String = "",
    val datosCompletados: Boolean = false,
    val altura: String = "",
    val genero: String = "",
    val peso: String = "",
    val experiencia: String = "",
    val nivelActividad: String = "",
    val objetivo: String = ""
)
