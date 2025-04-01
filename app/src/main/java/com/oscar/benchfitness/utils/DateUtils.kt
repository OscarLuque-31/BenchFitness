package com.oscar.benchfitness.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long, pattern: String = "dd/MM/yyyy"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(millis))
}

fun calcularEdad(birthday: String): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    // Convertir el string birthday en un objeto LocalDate
    val birthdayDate = LocalDate.parse(birthday, formatter)
    // Obtener la fecha actual
    val currentDate = LocalDate.now()
    // Calcular la diferencia en años, meses y días
    val period = Period.between(birthdayDate, currentDate)
    // Retornar la edad en años
    return period.years.toString()
}