package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.DayRoutine
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.ExerciseSet
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.models.statistics.StatisticsExercise
import com.oscar.benchfitness.models.user.userData
import com.oscar.benchfitness.repository.RoutineRepository
import com.oscar.benchfitness.repository.StatisticsExercisesRepository
import com.oscar.benchfitness.repository.UserRepository
import com.oscar.benchfitness.utils.CalorieCalculator
import com.oscar.benchfitness.utils.calcularEdad
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class HomeViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    var userData by mutableStateOf(userData())
    var calorias by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var rutinaSeleccionada by mutableStateOf(Routine())
    var rutinas by mutableStateOf(emptyList<Routine>())
    var isRutinaAsignada by mutableStateOf(false)
    var cambiarRutina by mutableStateOf(false)
    var apuntarRutina by mutableStateOf(false)
    var isRoutineLoading by mutableStateOf(false)
    var entrenamientoDelDia by mutableStateOf<DayRoutine?>(null)
    private val debounceJobs = mutableMapOf<String, Job>()
    val currentDate = java.text.SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(java.util.Date())


    // Estado para el progreso y datos de reps/peso
    var dailyExerciseProgress by mutableStateOf<List<ExerciseProgress>>(emptyList())
        private set

    private val routineRepository = RoutineRepository(auth, db)
    private val userRepository = UserRepository(auth, db)
    private val statisticsExercisesRepository = StatisticsExercisesRepository(auth, db)


    fun initDailyExerciseProgress(diaRutina: DayRoutine) {
        // Solo si no tienes progreso guardado, inicializa vacío
        if (dailyExerciseProgress.isEmpty()) {
            val ejerciciosProgreso = diaRutina.ejercicios.map { ejercicio ->
                ExerciseProgress(
                    nombre = ejercicio.nombre,
                    historial = listOf(
                        StatisticsExercise(
                            fecha = currentDate,
                            series = List(ejercicio.series) { ExerciseSet(reps = 0, peso = 0.0) },
                            completado = false
                        )
                    )
                )
            }
            dailyExerciseProgress = ejerciciosProgreso
        }
    }




    private fun updateSet(
        exerciseName: String,
        setIndex: Int,
        reps: Int? = null,
        weight: Double? = null
    ) {
        val updatedProgressList = dailyExerciseProgress.map { progress ->
            if (progress.nombre == exerciseName) {
                val updatedHistorial = progress.historial.map { ejecucion ->
                    if (ejecucion.fecha == currentDate) {
                        val updatedSeries = ejecucion.series.toMutableList()
                        val setAnterior = updatedSeries[setIndex]
                        updatedSeries[setIndex] = setAnterior.copy(
                            reps = reps ?: setAnterior.reps,
                            peso = weight ?: setAnterior.peso
                        )
                        ejecucion.copy(series = updatedSeries)
                    } else {
                        ejecucion
                    }
                }

                viewModelScope.launch {
                    statisticsExercisesRepository.saveOrUpdateExerciseExecution(
                        exerciseName = progress.nombre,
                        newProgress = updatedHistorial.first { it.fecha == currentDate }
                    )
                }

                progress.copy(historial = updatedHistorial)
            } else {
                progress
            }
        }

        dailyExerciseProgress = updatedProgressList
    }


    fun marcarEjercicioCompletado(exerciseName: String) {
        val updatedProgressList = dailyExerciseProgress.map { progress ->
            if (progress.nombre == exerciseName) {
                val updatedHistorial = progress.historial.map { ejecucion ->
                    if (ejecucion.fecha == currentDate) {
                        ejecucion.copy(completado = true)
                    } else {
                        ejecucion
                    }
                }

                viewModelScope.launch {
                    statisticsExercisesRepository.saveOrUpdateExerciseExecution(
                        exerciseName = progress.nombre,
                        newProgress = updatedHistorial.first { it.fecha == currentDate }
                    )
                }

                progress.copy(historial = updatedHistorial)
            } else {
                progress
            }
        }

        dailyExerciseProgress = updatedProgressList
    }



    suspend fun cargarDatosUsuario() {
        isLoading = true
        userData = userRepository.getUserInformation()
        rutinas = routineRepository.getAllRoutines()
        calorias = calcularDatos(userData)

        if (userData.rutinaAsignada != null) {
            cargarDiaEntrenamiento()
        } else {
            entrenamientoDelDia = null
        }

        isLoading = false
    }


    private fun calcularDatos(user: userData): String {
        return CalorieCalculator().calcularCaloriasConObjetivo(
            objetivo = user.objetivo,
            altura = user.altura,
            peso = user.peso,
            nivelActividad = user.nivelActividad,
            genero = user.genero,
            edad = calcularEdad(user.birthday)
        )
    }


    fun onRepsInput(ejercicioNombre: String, setIndex: Int, input: String) {
        val key = "reps_${ejercicioNombre}_$setIndex"
        val parsed = input.toIntOrNull()
        if (parsed != null && parsed > 0) {
            debounceJobs[key]?.cancel()
            debounceJobs[key] = viewModelScope.launch {
                delay(500)
                updateSet(ejercicioNombre, setIndex, reps = parsed)
            }
        }
    }

    fun onPesoInput(ejercicioNombre: String, setIndex: Int, input: String) {
        val key = "peso_${ejercicioNombre}_$setIndex"
        val parsed = input.toDoubleOrNull()
        if (parsed != null && parsed > 0.0) {
            debounceJobs[key]?.cancel()
            debounceJobs[key] = viewModelScope.launch {
                delay(500)
                updateSet(ejercicioNombre, setIndex, weight = parsed)
            }
        }
    }


    fun isValidReps(input: String): Boolean {
        val parsed = input.toIntOrNull()
        return parsed != null && parsed > 0
    }

    fun isValidPeso(input: String): Boolean {
        val parsed = input.toDoubleOrNull()
        return parsed != null && parsed > 0.0
    }

    fun asignarRutina() {
        viewModelScope.launch {
            isRoutineLoading = true
            userRepository.asignarRutina(rutinaSeleccionada)
            userData = userRepository.getUserInformation()
            isRutinaAsignada = true
            isRoutineLoading = false
        }
    }

    fun comprobarRutinaAsignada() {
        viewModelScope.launch {
            isRutinaAsignada = userRepository.isRutinaAsignada()
        }
    }

    fun desasignarRutina() {
        viewModelScope.launch {
            userRepository.desasignarRutina()
            userData = userRepository.getUserInformation()
            rutinaSeleccionada = Routine()
            isRutinaAsignada = false
        }
    }

    fun cargarDiaEntrenamiento() {
        println("=== cargarDiaEntrenamiento() llamada ===")

        val hoy = LocalDate.now()
        val diaActual = hoy.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
            .replaceFirstChar { it.uppercase() }

        val rutina = userData.rutinaAsignada

        if (rutina != null) {
            entrenamientoDelDia = rutina.dias.firstOrNull { it.dia == diaActual }

            entrenamientoDelDia?.let { dia ->
                viewModelScope.launch {
                    val progresoEjercicios = dia.ejercicios.map { ejercicio ->
                        val progreso = statisticsExercisesRepository.getExerciseProgressByName(ejercicio.nombre)
                        val hoyProgress = progreso?.historial?.find { it.fecha == currentDate }
                        println("Ejercicio: ${ejercicio.nombre}, progreso hoy: $hoyProgress")


                        if (hoyProgress != null) {
                            ExerciseProgress(
                                nombre = ejercicio.nombre,
                                historial = listOf(hoyProgress)
                            )
                        } else {
                            ExerciseProgress(
                                nombre = ejercicio.nombre,
                                historial = listOf(
                                    StatisticsExercise(
                                        fecha = currentDate,
                                        series = List(ejercicio.series) { ExerciseSet(reps = 0, peso = 0.0) },
                                        completado = false
                                    )
                                )
                            )
                        }
                    }

                    dailyExerciseProgress = progresoEjercicios
                }
            }
        } else {
            entrenamientoDelDia = null
            dailyExerciseProgress = emptyList()
        }
    }

}




