package com.oscar.benchfitness.viewModels.statistics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.utils.CalorieCalculator
import java.util.Locale

class CalculosViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var peso by mutableStateOf("")
    var pesoEjercicio by mutableStateOf("")
    var altura by mutableStateOf("")
    var edad by mutableStateOf("")
    var nivelActividad by mutableStateOf("Nivel actividad física")
    var genero by mutableStateOf("Sexo")
    var calorias by mutableStateOf("")
    var repeticiones by mutableStateOf("")
    var rm by mutableDoubleStateOf(0.0)
    var isCalculated by mutableStateOf(false)  // Nueva variable para verificar si se calculó el RM


    var showDialog by mutableStateOf(false)
    var mensajeError by mutableStateOf("")

    val opcionesNivelActividad = listOf(
        "Sedentario (poco o ningún ejercicio)",
        "Ligera actividad (1-3 días/semana)",
        "Actividad moderada (3-5 días/semana)",
        "Alta actividad (6-7 días/semana)",
        "Actividad muy intensa (entrenamientos extremos)"
    )
    val opcionesGenero = listOf("Hombre", "Mujer")

    // ======== VALIDACIONES =========

    fun esEdadValida(): Boolean {
        val e = edad.toIntOrNull() ?: return false
        return e in 5..120
    }

    fun esPesoValido(): Boolean {
        val p = peso.toFloatOrNull() ?: return false
        return p in 20f..300f
    }

    fun esAlturaValida(): Boolean {
        val a = altura.toFloatOrNull() ?: return false
        return a in 80f..250f
    }

    fun esPesoEjercicioValido(): Boolean {
        val p = pesoEjercicio.toFloatOrNull() ?: return false
        return p in 10f..300f
    }

    fun sonRepeticionesValidas(): Boolean {
        val r = repeticiones.toIntOrNull() ?: return false
        return r in 1..15
    }

    fun esFormularioCompletado(): Boolean {
        return peso.isNotBlank() && altura.isNotBlank() && edad.isNotBlank() &&
                nivelActividad in opcionesNivelActividad &&
                genero in opcionesGenero
    }

    fun esFormularioValido(): Boolean {
        return esFormularioCompletado() && esEdadValida() && esPesoValido() && esAlturaValida()
    }

    fun esFormularioRepeMaxCompletado(): Boolean {
        return pesoEjercicio.isNotBlank() && repeticiones.isNotBlank()
    }

    fun esFormularioRepeMaxValido(): Boolean {
        return esFormularioRepeMaxCompletado() && esPesoEjercicioValido() && sonRepeticionesValidas()
    }

    // ======== ACCIONES =========

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

    fun calcularRepeMaxima(){
        if (repeticiones.toInt() <= 0 || repeticiones.toInt() >= 37) rm = 0.0

        rm = String.format(Locale.US,"%.2f", pesoEjercicio.toDouble() / (1.0278 - (0.0278 * repeticiones.toInt()))).toDouble()
        isCalculated = true
    }
}
