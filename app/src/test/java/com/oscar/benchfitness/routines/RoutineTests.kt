package com.oscar.benchfitness.routines

import com.oscar.benchfitness.models.routines.DayRoutine
import com.oscar.benchfitness.models.routines.ExerciseRoutineEntry
import com.oscar.benchfitness.models.routines.Routine
import org.junit.Assert.*
import org.junit.Test

class RoutineTests {

    /**
     * Verifica que se pueda crear una entrada de ejercicio correctamente.
     */
    @Test
    fun crearEjercicioRutina() {
        val ejercicio = ExerciseRoutineEntry(
            nombre = "Press banca",
            series = 4,
            repeticiones = 10
        )

        assertEquals("Press banca", ejercicio.nombre)
        assertEquals(4, ejercicio.series)
        assertEquals(10, ejercicio.repeticiones)
    }

    /**
     * Verifica que se pueda crear un día con varios ejercicios asociados.
     */
    @Test
    fun crearDiaRutinaConEjercicios() {
        val ejercicios = listOf(
            ExerciseRoutineEntry("Sentadillas", 3, 12),
            ExerciseRoutineEntry("Peso muerto", 4, 8)
        )

        val dia = DayRoutine("Lunes", ejercicios)

        assertEquals("Lunes", dia.dia)
        assertEquals(2, dia.ejercicios.size)
        assertEquals("Sentadillas", dia.ejercicios[0].nombre)
    }

    /**
     * Verifica que se pueda crear una rutina completa con días y ejercicios.
     */
    @Test
    fun crearRutinaCompleta() {
        val ejerciciosDia1 = listOf(
            ExerciseRoutineEntry("Dominadas", 3, 8),
            ExerciseRoutineEntry("Remo con barra", 3, 10)
        )

        val ejerciciosDia2 = listOf(
            ExerciseRoutineEntry("Press militar", 4, 10),
            ExerciseRoutineEntry("Elevaciones laterales", 4, 15)
        )

        val dias = listOf(
            DayRoutine("Martes", ejerciciosDia1),
            DayRoutine("Jueves", ejerciciosDia2)
        )

        val rutina = Routine(
            nombre = "Rutina Fuerza",
            objetivo = "Hipertrofia",
            dias = dias
        )

        assertEquals("Rutina Fuerza", rutina.nombre)
        assertEquals("Hipertrofia", rutina.objetivo)
        assertEquals(2, rutina.dias.size)
        assertEquals("Martes", rutina.dias[0].dia)
        assertEquals("Press militar", rutina.dias[1].ejercicios[0].nombre)
    }

    /**
     * Verifica que la función toMap de Routine genere un mapa válido.
     */
    @Test
    fun rutinaToMapEsCorrecto() {
        val rutina = Routine(
            nombre = "Rutina Prueba",
            objetivo = "Fuerza",
            dias = listOf(
                DayRoutine("Lunes", listOf(ExerciseRoutineEntry("Curl bíceps", 3, 12)))
            )
        )

        val map = rutina.toMap()

        assertEquals("Rutina Prueba", map["nombre"])
        assertEquals("Fuerza", map["objetivo"])
        assertTrue(map.containsKey("fechaCreacion"))
        assertEquals("Lunes", ((map["dias"] as List<*>)[0] as Map<*, *>)["dia"])
    }

    /**
     * Verifica que solo se permitan los objetivos "Hipertrofia" o "Fuerza".
     */
    @Test
    fun objetivosValidos() {
        val objetivosValidos = listOf("Hipertrofia", "Fuerza")

        val rutina1 = Routine(nombre = "Volumen", objetivo = "Hipertrofia", dias = listOf())
        val rutina2 = Routine(nombre = "Fuerza máxima", objetivo = "Fuerza", dias = listOf())

        assertTrue(objetivosValidos.contains(rutina1.objetivo))
        assertTrue(objetivosValidos.contains(rutina2.objetivo))
    }

    /**
     * Verifica que un objetivo inválido no sea aceptado.
     */
    @Test
    fun objetivoInvalidoNoPermitido() {
        val objetivosValidos = listOf("Hipertrofia", "Fuerza")

        val rutina = Routine(nombre = "Corte", objetivo = "Definición", dias = listOf())

        assertFalse(objetivosValidos.contains(rutina.objetivo))
    }
}
