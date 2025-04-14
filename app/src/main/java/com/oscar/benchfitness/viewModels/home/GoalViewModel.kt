package com.oscar.benchfitness.viewModels.home

import androidx.lifecycle.ViewModel
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.utils.CalorieCalculator
import com.oscar.benchfitness.utils.calcularEdad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoalViewModel(userData: userData) : ViewModel() {

    // --- Definición ---
    val TEXTO_DEFINICION_DF =
        "Es cuando consumes menos calorías de las que tu cuerpo necesita para mantener su peso. Esto obliga a tu cuerpo a usar sus reservas de energía (grasa) para cubrir la diferencia."

    val TEXTO_COMO_LOGRARLO_DF =
        "\nReducir calorías: Comer un poco menos o elegir alimentos con menos calorías.\nAumentar actividad: Hacer más ejercicio o mover más el cuerpo."

    val TEXTO_SEGURIDAD_DF =
        "Un déficit de 300 calorías al día es ideal para perder peso de forma saludable y sostenible."

    val TEXTO_RECUERDA_DF =
        "No es bueno hacer un déficit demasiado grande, ya que puede afectar la masa muscular y la energía."

    // --- Mantenimiento ---
    val TEXTO_MANTENIMIENTO_MT =
        "Es cuando consumes aproximadamente la misma cantidad de calorías que tu cuerpo necesita para mantener su peso actual. No se gana ni se pierde peso."

    val TEXTO_COMO_LOGRARLO_MT =
        "\nCalorías equilibradas: Comer lo suficiente para cubrir tus necesidades energéticas.\nActividad constante: Mantener un nivel regular de ejercicio y movimiento diario."

    val TEXTO_SEGURIDAD_MT =
        "Mantener tu peso puede ser ideal si estás contento con tu físico actual o quieres enfocarte en ganar fuerza o salud sin cambios de peso."

    val TEXTO_RECUERDA_MT =
        "Aunque no estés subiendo o bajando de peso, la calidad de los alimentos y el ejercicio siguen siendo clave para tu bienestar."

    // --- Volumen ---
    val TEXTO_VOLUMEN_VL =
        "Es cuando consumes más calorías de las que tu cuerpo necesita para mantener su peso. Esto le da al cuerpo energía extra para construir músculo."

    val TEXTO_COMO_LOGRARLO_VL =
        "\nSuperávit calórico: Comer más calorías, especialmente con alimentos ricos en nutrientes.\nEntrenamiento de fuerza: Hacer ejercicios que estimulen el crecimiento muscular."

    val TEXTO_SEGURIDAD_VL =
        "Un superávit de 250 a 350 calorías al día es suficiente para ganar masa muscular sin aumentar mucho la grasa."

    val TEXTO_RECUERDA_VL =
        "No se trata solo de comer más, sino de comer mejor. Elige proteínas, carbohidratos complejos y grasas saludables para un volumen limpio."


    // Calorias

    private val _caloriasDefi = MutableStateFlow("")
    val caloriasDefi = _caloriasDefi.asStateFlow()

    private val _caloriasVol = MutableStateFlow("")
    val caloriasVol = _caloriasVol.asStateFlow()

    private val _caloriasMan = MutableStateFlow("")
    val caloriasMan = _caloriasMan.asStateFlow()

    private val _caloriasMeta = MutableStateFlow("")
    val caloriasMeta = _caloriasMeta.asStateFlow()


    /**
     * Método que calcula las calorías de todos los objetivos para que el usuario
     * tenga la información necesario
     */
    fun calcularTodasLasCalorias(userData: userData) {

        // Calorias en definicion
        _caloriasDefi.value = CalorieCalculator().calcularCaloriasConObjetivo(
            "Perder peso",
            altura = userData.altura,
            edad = calcularEdad(userData.birthday),
            genero = userData.genero,
            peso = userData.peso,
            nivelActividad = userData.nivelActividad
        )

        // Calorias en mantenimiento
        _caloriasMan.value = CalorieCalculator().calcularCaloriasConObjetivo(
            "Mantener peso",
            altura = userData.altura,
            edad = calcularEdad(userData.birthday),
            genero = userData.genero,
            peso = userData.peso,
            nivelActividad = userData.nivelActividad
        )

        // Calorías en volumen
        _caloriasVol.value = CalorieCalculator().calcularCaloriasConObjetivo(
            "Masa muscular",
            altura = userData.altura,
            edad = calcularEdad(userData.birthday),
            genero = userData.genero,
            peso = userData.peso,
            nivelActividad = userData.nivelActividad
        )

        // Calorías de tu metabolismo basal
        _caloriasMeta.value = CalorieCalculator().calcularMetabolismo(
            altura = userData.altura,
            edad = calcularEdad(userData.birthday),
            genero = userData.genero,
            peso = userData.peso,
        )

    }


}
