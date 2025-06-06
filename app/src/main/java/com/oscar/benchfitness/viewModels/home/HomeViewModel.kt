package com.oscar.benchfitness.viewModels.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.DayRoutine
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.ExerciseSet
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class HomeViewModel(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) : ViewModel() {

    // Variables necesarias para la pantalla Home
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
    val currentDate: String =
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(java.util.Date())

    // Evita guardar en base de datos en cada pulsación (debounce por campo)
    private val debounceJobs = mutableMapOf<String, Job>()

    // Progreso diario de ejercicios
    var dailyExerciseProgress by mutableStateOf<List<ExerciseProgress>>(emptyList())
        private set

    // Repositorios para guardar los datos en base de datos
    private val routineRepository = RoutineRepository(auth, db)
    private val userRepository = UserRepository(auth, db)
    private val statisticsExercisesRepository = StatisticsExercisesRepository(auth, db)


    /**
     * Inicializa el progreso de los ejercicios del día
     */
    fun initDailyExerciseProgress(diaRutina: DayRoutine) {
        // Si no tiene ningun progreso guardado, inicializa el ejercicio vacio
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
            // Asigna el progreso diario
            dailyExerciseProgress = ejerciciosProgreso
        }
    }

    /**
     * Método que guarda o actualiza el progreso de una serie de un ejercicio
     */
    private fun updateSet(
        exerciseName: String,
        setIndex: Int,
        reps: Int? = null,
        weight: Double? = null
    ) {
        // Itera el progreso diario
        val updatedProgressList = dailyExerciseProgress.map { progress ->
            // Busca el progreso del ejercicio en concreto
            if (progress.nombre == exerciseName) {
                val updatedHistorial = progress.historial.map { ejecucion ->
                    // Si la fecha coincide con la de hoy se actualiza la serie
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
                    // Se guarda en base de datos la ejecución de la serie
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
        // Se actualiza el progreso diario
        dailyExerciseProgress = updatedProgressList
    }

    /**
     * Método que marca un ejercicio como completado cuando las series estan completadas
     */
    fun marcarEjercicioCompletado(exerciseName: String) {
        val updatedProgressList = dailyExerciseProgress.map { progress ->
            if (progress.nombre == exerciseName) {
                // Itera sobre el historial de hoy y marca como completado
                val updatedHistorial = progress.historial.map { ejecucion ->
                    if (ejecucion.fecha == currentDate) {
                        ejecucion.copy(completado = true)
                    } else {
                        ejecucion
                    }
                }

                viewModelScope.launch {
                    // Guarda el progreso en base de datos
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
        // Actualiza el progreso
        dailyExerciseProgress = updatedProgressList
    }

    /**
     * Método que carga todos los datos del usuario necesarios
     */
    suspend fun cargarDatosUsuario() {
        isLoading = true
        // Carga la información del usuario
        userData = userRepository.getUserInformation()
        // Carga sus rutinas
        rutinas = routineRepository.getAllRoutines()
        // Carga las calorias del usuario
        calorias = calcularDatos(userData)

        if (userData.rutinaAsignada != null) {
            cargarDiaEntrenamiento()
        } else {
            entrenamientoDelDia = null
        }

        isLoading = false
    }


    /**
     * Método que calcula las calorias del usuario en base a sus datos
     */
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


    /**
     * Método que por cada input de reps que haya y se introduzcan datos cada 500 milisegundos cuando
     * se pare de escribir se guardará en base de datos para que los datos no desaparezcan
     */
    fun onRepsInput(ejercicioNombre: String, setIndex: Int, input: String) {
        val key = "reps_${ejercicioNombre}_$setIndex"
        val parsed = input.toIntOrNull()
        if (parsed != null && parsed > 0) {
            debounceJobs[key]?.cancel()
            debounceJobs[key] = viewModelScope.launch {
                // Espera 500 milisegundos
                delay(500)
                // Actualiza la serie
                updateSet(ejercicioNombre, setIndex, reps = parsed)
            }
        }
    }

    /**
     * Método que por cada input de peso que haya y se introduzcan datos cada 500 milisegundos cuando
     * se pare de escribir se guardará en base de datos para que los datos no desaparezcan
     */
    fun onPesoInput(ejercicioNombre: String, setIndex: Int, input: String) {
        val key = "peso_${ejercicioNombre}_$setIndex"
        val parsed = input.toDoubleOrNull()
        if (parsed != null && parsed > 0.0) {
            debounceJobs[key]?.cancel()
            debounceJobs[key] = viewModelScope.launch {
                // Espera 500 milisegundos
                delay(500)
                // Actualiza la serie
                updateSet(ejercicioNombre, setIndex, weight = parsed)
            }
        }
    }

    /**
     * Método que valida si las reps son validas
     */
    fun isValidReps(input: String): Boolean {
        val parsed = input.toIntOrNull()
        return parsed != null && parsed > 0
    }

    /**
     * Método que valida si el peso es valido
     */
    fun isValidPeso(input: String): Boolean {
        val parsed = input.toDoubleOrNull()
        return parsed != null && parsed > 0.0
    }

    /**
     * Método que asigna la rutina al usuario
     */
    fun asignarRutina() {
        viewModelScope.launch {
            isRoutineLoading = true
            // Se asigna la rutina en base de datos
            userRepository.asignarRutina(rutinaSeleccionada)
            // Se vuelve a cargar los datos de usuario para mostrar la rutina asignada
            userData = userRepository.getUserInformation()
            isRutinaAsignada = true
            isRoutineLoading = false
        }
    }

    /**
     * Método que comprueba si la rutina esta asignada
     */
    fun comprobarRutinaAsignada() {
        viewModelScope.launch {
            isRutinaAsignada = userRepository.isRutinaAsignada()
        }
    }

    /**
     * Método que desasigna la rutina del usuario
     */
    fun desasignarRutina() {
        viewModelScope.launch {
            // Desasigna la rutina
            userRepository.desasignarRutina()
            // Vuelve a cargar los datos del usuario
            userData = userRepository.getUserInformation()
            rutinaSeleccionada = Routine()
            isRutinaAsignada = false
        }
    }

    /**
     * Método que carga el dia de entrenamiento de la rutina en concreto
     */
    fun cargarDiaEntrenamiento() {
        val hoy = LocalDate.now()
        val diaActual = hoy.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
            .replaceFirstChar { it.uppercase() }

        // Obtien la rutina asignada por el usuario
        val rutina = userData.rutinaAsignada

        if (rutina != null) {
            // Coge el entrenamiento del dia actual
            entrenamientoDelDia = rutina.dias.firstOrNull { it.dia == diaActual }

            // Se itera el dia de entrenamiento
            entrenamientoDelDia?.let { dia ->
                viewModelScope.launch {
                    val progresoEjercicios = dia.ejercicios.map { ejercicio ->
                        // Obtiene el progreso del ejercicio actual
                        val progreso =
                            statisticsExercisesRepository.getExerciseProgressByName(ejercicio.nombre)
                        // Comprueba si hay progreso hoy
                        val hoyProgress = progreso?.historial?.find { it.fecha == currentDate }

                        // Si hay progreso se asigna
                        if (hoyProgress != null) {
                            ExerciseProgress(
                                nombre = ejercicio.nombre,
                                historial = listOf(hoyProgress)
                            )
                            // Si no hay progreso se inicializa a 0
                        } else {
                            ExerciseProgress(
                                nombre = ejercicio.nombre,
                                historial = listOf(
                                    StatisticsExercise(
                                        fecha = currentDate,
                                        series = List(ejercicio.series) {
                                            ExerciseSet(
                                                reps = 0,
                                                peso = 0.0
                                            )
                                        },
                                        completado = false
                                    )
                                )
                            )
                        }
                    }
                    // Se asigna el progreso del ejercicio al progreso diario
                    dailyExerciseProgress = progresoEjercicios
                }
            }
        } else {
            entrenamientoDelDia = null
            dailyExerciseProgress = emptyList()
        }
    }
}