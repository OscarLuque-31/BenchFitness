package com.oscar.benchfitness.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class Routine(
    val nombre: String = "",
    val objetivo: String = "",
    val dias: List<DayRoutine> = emptyList(),
    val fechaCreacion: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
) : Parcelable {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "nombre" to nombre,
            "objetivo" to objetivo,
            "dias" to dias.map { it.toMap() },
            "fechaCreacion" to fechaCreacion
        )
    }
}
