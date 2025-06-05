package com.oscar.benchfitness.utils

import com.oscar.benchfitness.models.routines.DayRoutine
import com.oscar.benchfitness.models.routines.ExerciseRoutineEntry
import com.oscar.benchfitness.models.routines.Routine


// Rutina para Principiantes (3 días/semana - Full Body)
fun crearRutinaPrincipiante(): Routine {
    return Routine(
        nombre = "R Principiante",
        objetivo = "Hipertrofia",
        dias = listOf(
            DayRoutine( // Día 1
                dia = "Lunes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press en máquina para pecho", 3, 10),
                    ExerciseRoutineEntry("Remo máquina agarre ancho", 3, 10),
                    ExerciseRoutineEntry("Sentadilla en máquina Smith", 3, 10),
                    ExerciseRoutineEntry("Curl con barra", 2, 12),
                    ExerciseRoutineEntry("Extensión de tríceps en máquina", 2, 12),
                    ExerciseRoutineEntry("Abdominales", 3, 15)
                )
            ),
            DayRoutine( // Día 2
                dia = "Miércoles",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press militar en máquina", 3, 10),
                    ExerciseRoutineEntry("Jalón al pecho", 3, 10),
                    ExerciseRoutineEntry("Prensa", 3, 10),
                    ExerciseRoutineEntry("Curl con mancuerna", 2, 12),
                    ExerciseRoutineEntry("Fondos en banco", 2, 10),
                    ExerciseRoutineEntry("Crunch cruzado", 3, 15)
                )
            ),
            DayRoutine( // Día 3
                dia = "Viernes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press con mancuernas", 3, 10),
                    ExerciseRoutineEntry("Remo con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Sentadilla con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Curl de bíceps martillo", 2, 12),
                    ExerciseRoutineEntry("Tríceps en polea", 2, 12),
                    ExerciseRoutineEntry("Crunches inferiores", 3, 15)
                )
            )
        )
    )
}

// Rutina para Intermedios (4 días/semana - Torso/Pierna)
fun crearRutinaIntermedio(): Routine {
    return Routine(
        nombre = "R Intermedia",
        objetivo = "Hipertrofia",
        dias = listOf(
            DayRoutine( // Día 1: Empuje (pecho, hombros, tríceps)
                dia = "Lunes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press de banca", 3, 8),
                    ExerciseRoutineEntry("Press inclinado con mancuernas", 3, 10),
                    ExerciseRoutineEntry("Press militar con barra de pie", 3, 8),
                    ExerciseRoutineEntry("Fondos de pecho", 3, 10),
                    ExerciseRoutineEntry("Elevaciones laterales", 3, 10)
                )
            ),
            DayRoutine( // Día 2: Piernas
                dia = "Martes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Sentadilla libre", 4, 8),
                    ExerciseRoutineEntry("Peso muerto rumano con barra", 3, 8),
                    ExerciseRoutineEntry("Extensión de cuádriceps", 3, 12),
                    ExerciseRoutineEntry("Femorales en máquina tumbado", 3, 12),
                    ExerciseRoutineEntry("Gemelo en pie", 3, 15)
                )
            ),
            DayRoutine( // Día 3: Tirón (espalda, bíceps)
                dia = "Jueves",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Jalón al pecho", 4, 8),
                    ExerciseRoutineEntry("Remo T", 3, 8),
                    ExerciseRoutineEntry("Remo con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Curl predicador con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Encogimiento con mancuerna sentado", 3, 12)
                )
            ),
            DayRoutine( // Día 4: Full Body
                dia = "Viernes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press inclinado", 3, 8),
                    ExerciseRoutineEntry("Remo en punta", 3, 10),
                    ExerciseRoutineEntry("Sentadilla búlgara", 3, 8),
                    ExerciseRoutineEntry("Curl concentrado", 3, 10),
                    ExerciseRoutineEntry("Press francés con barra", 3, 10)
                )
            )
        )
    )
}

// Rutina para Avanzados (5 días/semana - División por grupos musculares)
fun crearRutinaAvanzado(): Routine {
    return Routine(
        nombre = "R Avanzada",
        objetivo = "Hipertrofia",
        dias = listOf(
            DayRoutine( // Día 1: Pecho
                dia = "Lunes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press de banca", 4, 6),
                    ExerciseRoutineEntry("Press inclinado con mancuernas", 3, 8),
                    ExerciseRoutineEntry("Aperturas con mancuerna", 3, 12),
                    ExerciseRoutineEntry("Fondos lastrados", 3, 8),
                    ExerciseRoutineEntry("Cruce de poleas alta", 2, 15)
                )
            ),
            DayRoutine( // Día 2: Espalda
                dia = "Martes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Peso muerto rumano con barra", 4, 5),
                    ExerciseRoutineEntry("Remo T", 3, 8),
                    ExerciseRoutineEntry("Jalón al pecho invertido", 3, 10),
                    ExerciseRoutineEntry("Remo con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Encogimiento en paralelas", 3, 12)
                )
            ),
            DayRoutine( // Día 3: Piernas
                dia = "Miércoles",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Sentadilla libre con postura abierta", 4, 6),
                    ExerciseRoutineEntry("Hack Squat", 3, 8),
                    ExerciseRoutineEntry("Femorales unilaterales", 3, 10),
                    ExerciseRoutineEntry("Peso muerto rumano a una pierna", 3, 8),
                    ExerciseRoutineEntry("Elevación de talones en máquina", 4, 15)
                )
            ),
            DayRoutine( // Día 4: Hombros y abdominales
                dia = "Jueves",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Press militar con barra de pie", 4, 6),
                    ExerciseRoutineEntry("Elevaciones laterales en polea", 3, 12),
                    ExerciseRoutineEntry("Vuelos posteriores", 3, 12),
                    ExerciseRoutineEntry("Crunches en polea", 3, 15),
                    ExerciseRoutineEntry("Crunches en máquina", 3, 15)
                )
            ),
            DayRoutine( // Día 5: Brazos
                dia = "Viernes",
                ejercicios = listOf(
                    ExerciseRoutineEntry("Curl bayesian", 4, 8),
                    ExerciseRoutineEntry("Curl predicador con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Press francés unilaterales con mancuerna", 3, 10),
                    ExerciseRoutineEntry("Tríceps en polea alta", 3, 12),
                    ExerciseRoutineEntry("Patada de tríceps en polea", 3, 12)
                )
            )
        )
    )
}