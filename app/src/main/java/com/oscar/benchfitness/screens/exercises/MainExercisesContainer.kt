package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.R
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.navegation.Ejercicio
import com.oscar.benchfitness.navegation.Ejercicios
import com.oscar.benchfitness.navegation.Favs
import com.oscar.benchfitness.navegation.Rutinas
import com.oscar.benchfitness.repository.ExercisesRepository
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.exercises.EjercicioViewModel
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel

@Composable
fun MainExercisesContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val innerNavController = rememberNavController()

    Scaffold(containerColor = negroBench, topBar = {

        CabeceraOpcionesEjerciciosScreen(innerNavController)

    }) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = Ejercicios,
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = 0.dp
            )
        ) {
            composable<Ejercicios> {
                val ejerciciosViewModel = remember { EjerciciosViewModel(auth, db) }

                LaunchedEffect(Unit) {
                    ejerciciosViewModel.cargarEjercicios()
                }

                EjerciciosScreen(
                    navController = innerNavController,
                    viewModel = ejerciciosViewModel,
                )
            }
            composable<Ejercicio> {
                val ejercicioViewModel = remember { EjercicioViewModel(auth, db) }

                val ejercicio = innerNavController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ExerciseData>("ejercicio")

                var exercisesRepository = ExercisesRepository()

                // Estado para manejar la URL del GIF
                var urlGIF by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(ejercicio) {
                    ejercicio?.let {

                        println(it.url_image.replace("+"," "))

                        urlGIF = exercisesRepository.obtenerURLFirmadaGif(it.url_image.replace("+"," "))

                        println(urlGIF)
                    }
                }

                if (ejercicio != null) {
                    EjercicioScreen(
                        navController = innerNavController,
                        viewModel = ejercicioViewModel,
                        ejercicio = ejercicio,
                        urlGIF = urlGIF ?: ""
                    )
                }
            }
            composable<Rutinas> {

            }
            composable<Favs> {

            }
        }
    }
}

@Composable
fun CabeceraOpcionesEjerciciosScreen(navController: NavController) {

    var selectedOption by remember { mutableStateOf("Ejercicios") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.End
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_marca),
            contentDescription = "Logo aplicación",
            modifier = Modifier.size(100.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExerciseHeaderOption(
                text = "Ejercicios",
                selected = selectedOption == "Ejercicios",
                onClick = {
                    selectedOption = "Ejercicios"
                    navController.navigate(Ejercicios)
                }
            )

            ExerciseHeaderOption(
                text = "Rutina",
                selected = selectedOption == "Rutina",
                onClick = {
                    selectedOption = "Rutina"
                    navController.navigate(Rutinas)
                }
            )

            ExerciseHeaderOption(
                text = "Favs",
                selected = selectedOption == "Favs",
                onClick = {
                    selectedOption = "Favs"
                    navController.navigate(Favs)
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(rojoBench)
        )
    }
}


@Composable
fun ExerciseHeaderOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        style = MaterialTheme.typography.bodySmall,
        fontSize = 24.sp,
        color = if (selected) rojoBench else Color.White
    )
}



