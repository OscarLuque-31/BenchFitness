package com.oscar.benchfitness.models.exercises

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Clase que representa un ejercicio con todos sus datos
 */
@Parcelize
data class ExerciseData(
    var id_ejercicio: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var categoria: String = "",
    var nivel: String = "",
    var equipamiento: String = "",
    var tipo_fuerza: String = "",
    var musculo_principal: String = "",
    var musculo_secundario: String = "",
    var instrucciones: String = "",
    var url_image: String = ""
) : Parcelable
