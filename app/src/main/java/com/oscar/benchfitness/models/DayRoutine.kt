package com.oscar.benchfitness.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DayRoutine (
    val dia: String = "",
    val ejercicios : List<ExerciseRoutineEntry> = emptyList()
) : Parcelable{
    fun toMap(): Map<String, Any> {
        return mapOf(
            "dia" to dia,
            "ejercicios" to ejercicios.map { it.toMap() }
        )
    }
}