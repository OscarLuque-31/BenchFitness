package com.oscar.benchfitness.screens.workout.exercises

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.navegation.Ejercicio
import com.oscar.benchfitness.navegation.Ejercicios
import com.oscar.benchfitness.repository.ExercisesRepository
import com.oscar.benchfitness.viewModels.workout.EjercicioViewModel
import com.oscar.benchfitness.viewModels.workout.EjerciciosViewModel


@Composable
fun MainExercisesContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
) {
    // Controlador de la navegación de los ejercicios
    val navController = rememberNavController()

    NavHost(navController, startDestination = Ejercicios.route) {

        composable(Ejercicios.route) {
            val ejerciciosViewModel = remember { EjerciciosViewModel(auth, db) }

            LaunchedEffect(Unit) {
                ejerciciosViewModel.cargarEjercicios()
            }

            EjerciciosScreen(
                navController = navController,
                viewModel = ejerciciosViewModel,
            )
        }

        composable(Ejercicio.route) {
            val ejercicioViewModel = remember { EjercicioViewModel(auth, db) }

            // Recojo el ejercicio enviado a través del controlador
            val ejercicio = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<ExerciseData>("ejercicio")

            var exercisesRepository = ExercisesRepository()

            // Estado para manejar la URL del GIF
            var urlGIF by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(ejercicio) {
                ejercicio?.let {
                    // Obtiene la url firmada desde AWS Bucket S3
                    urlGIF = exercisesRepository.obtenerURLFirmadaGif(it.url_image.replace("+", " "))
                }
            }

            // Si el ejercicio no es nulo lo muestra
            if (ejercicio != null) {
                EjercicioScreen(
                    navController = navController,
                    viewModel = ejercicioViewModel,
                    ejercicio = ejercicio,
                    urlGIF = urlGIF ?: ""
                )
            }
        }
    }
}