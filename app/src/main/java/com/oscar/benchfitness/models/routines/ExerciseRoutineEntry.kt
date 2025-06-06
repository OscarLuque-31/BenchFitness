package com.oscar.benchfitness.models.routines

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Clase que representa la entrada de un ejercicio en la rutina
 */
@Parcelize
data class ExerciseRoutineEntry(
    val nombre: String = "",
    val series: Int = 0,
    val repeticiones: Int = 0
) : Parcelable {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "series" to series,
            "repeticiones" to repeticiones
        )
    }
}