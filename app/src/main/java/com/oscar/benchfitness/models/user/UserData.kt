package com.oscar.benchfitness.models.user

import android.os.Parcelable
import com.oscar.benchfitness.models.routines.Routine
import kotlinx.parcelize.Parcelize

@Parcelize
// Clase que representa los datos que se recogen del usuario
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
    val objetivo: String = "",
    val rutinaAsignada: Routine = Routine()
) : Parcelable
