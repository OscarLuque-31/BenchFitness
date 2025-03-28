package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class PrincipalViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val INDICE_PESO = 10
    private val INDICE_ALTURA = 6.25
    private val INDICE_EDAD = 5
    private val INDICE_GENERO_HOMBRE = 5
    private val INDICE_GENERO_MUJER = 5
    private val INDICE_ACTIVIDAD_SEDENTARIO = 1.2
    private val INDICE_ACTIVIDAD_LIGERO = 1.375
    private val INDICE_ACTIVIDAD_MODERADO = 1.55
    private val INDICE_ACTIVIDAD_FUERTE = 1.725
    private val INDICE_ACTIVIDAD_MUYFUERTE = 1.9

    var nombre by mutableStateOf("")
    var objetivo by mutableStateOf("")
    var calorias by mutableStateOf("")
    var peso by mutableStateOf("")
    var nivelActividad by mutableStateOf("")
    var genero by mutableStateOf("")
    var experiencia by mutableStateOf("")
    var birthday by mutableStateOf("")
    var altura by mutableStateOf("")

    fun cargarDatosUsuario() {
        val user = auth.currentUser
        user?.let {
            db.collection("users").document(user.uid).get().addOnSuccessListener { document ->
                nombre = document.getString("username").toString()
                objetivo = document.getString("objetivo").toString()
                peso = document.getString("peso").toString()
                nivelActividad = document.getString("nivelActividad").toString()
                genero = document.getString("genero").toString()
                birthday = document.getString("birthday").toString()
                altura = document.getString("altura").toString()

                calorias = calcularCaloriasConObjetivo(
                    objetivo = objetivo,
                    altura = altura,
                    peso = peso,
                    nivelActividad = nivelActividad,
                    genero = genero,
                    edad = calcularEdad(birthday)
                )
            }
        }
    }

    private fun calcularCaloriasConObjetivo(
        objetivo: String,
        altura: String,
        genero: String,
        nivelActividad: String,
        peso: String,
        edad: String
    ): String {
        return when (objetivo) {
            "Perder peso" -> (calcularMetabolismoTotal(
                altura,
                genero,
                nivelActividad,
                peso,
                edad
            ) - 200).toString()

            "Mantener peso" -> (calcularMetabolismoTotal(
                altura,
                genero,
                nivelActividad,
                peso,
                edad
            )).toString()

            "Masa muscular" -> (calcularMetabolismoTotal(
                altura,
                genero,
                nivelActividad,
                peso,
                edad
            ) + 200).toString()

            else -> "Desconocido"
        }
    }

    /**
     * Método que calcula el metabolismo total multiplicado con el índice de actividad
     * Se calcula el metabolismo con la fórmula de Harris Benedict
     */
    private fun calcularMetabolismoTotal(
        altura: String,
        genero: String,
        nivelActividad: String,
        peso: String,
        edad: String
    ): Double {

        if (genero.equals("Hombre")) {
            return ((INDICE_PESO * peso.toDouble()) + (INDICE_ALTURA * altura.toInt()) - (INDICE_EDAD * edad.toInt()) + INDICE_GENERO_HOMBRE) * indiceNivelActividad(
                nivelActividad
            )
        } else {
            return ((INDICE_PESO * peso.toDouble()) + (INDICE_ALTURA * altura.toInt()) - (INDICE_EDAD * edad.toInt()) - INDICE_GENERO_MUJER) * indiceNivelActividad(
                nivelActividad
            )
        }

    }

    /**
     * Método que calcula el indice de nivel de actividad en base a su nivel de actividad
     */
    private fun indiceNivelActividad(nivelActividad: String): Double {
        return when (nivelActividad) {
            "Sedentario (poco o ningún ejercicio)" -> return INDICE_ACTIVIDAD_SEDENTARIO
            "Ligera actividad (1-3 días/semana)" -> return INDICE_ACTIVIDAD_LIGERO
            "Actividad moderada (3-5 días/semana)" -> return INDICE_ACTIVIDAD_MODERADO
            "Alta actividad (6-7 días/semana)" -> return INDICE_ACTIVIDAD_FUERTE
            "Actividad muy intensa (entrenamientos extremos)" -> return INDICE_ACTIVIDAD_MUYFUERTE
            else -> return 1.0
        }
    }

    fun interpretarObjetivo(objetivo: String): String {
        return when (objetivo) {
            "Perder peso" -> "Déficit"
            "Mantener peso" -> "Mantener"
            "Masa muscular" -> "Superávit"
            else -> "Desconocido"
        }
    }

    private fun calcularEdad(birthday: String): String {

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
}

