package com.oscar.benchfitness.utils

class CalorieCalculator {

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

    /**
     * Método que calcula las calorías de una persona según su objetivo fitness
     */
    fun calcularCaloriasConObjetivo(
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
}
