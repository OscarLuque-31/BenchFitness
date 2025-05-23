package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.R
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.navegation.CrearRutina
import com.oscar.benchfitness.navegation.Ejercicio
import com.oscar.benchfitness.navegation.Ejercicios
import com.oscar.benchfitness.navegation.Favs
import com.oscar.benchfitness.navegation.Rutina
import com.oscar.benchfitness.navegation.Rutinas
import com.oscar.benchfitness.repository.ExercisesRepository
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.exercises.CrearRutinaViewModel
import com.oscar.benchfitness.viewModels.exercises.EjercicioViewModel
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel
import com.oscar.benchfitness.viewModels.exercises.FavsViewModel
import com.oscar.benchfitness.viewModels.exercises.RutinaViewModel
import com.oscar.benchfitness.viewModels.exercises.RutinasViewModel

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
            startDestination = Ejercicios.route,
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = 0.dp
            )
        ) {
            composable(Ejercicios.route) {
                val ejerciciosViewModel = remember { EjerciciosViewModel(auth, db) }

                LaunchedEffect(Unit) {
                    ejerciciosViewModel.cargarEjercicios()
                }

                EjerciciosScreen(
                    navController = innerNavController,
                    viewModel = ejerciciosViewModel,
                )
            }
            composable(Ejercicio.route) {
                val ejercicioViewModel = remember { EjercicioViewModel(auth, db) }

                val ejercicio = innerNavController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ExerciseData>("ejercicio")

                var exercisesRepository = ExercisesRepository()

                // Estado para manejar la URL del GIF
                var urlGIF by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(ejercicio) {
                    ejercicio?.let {

                        println(it.url_image.replace("+", " "))

                        urlGIF =
                            exercisesRepository.obtenerURLFirmadaGif(it.url_image.replace("+", " "))

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
            composable(Rutinas.route) {
                val rutinaViewModel = remember { RutinasViewModel(auth, db) }

                LaunchedEffect(Unit) {
                    rutinaViewModel.obtenerRutinas()
                }

                RutinasScreen(innerNavController, rutinaViewModel)
            }
            composable(CrearRutina.route) {

                val crearRutinaViewModel = remember { CrearRutinaViewModel(auth, db) }
                CrearRutinaScreen(innerNavController, crearRutinaViewModel)

            }
            composable(Rutina.route) {

                val rutinaViewModel = remember { RutinaViewModel(auth, db) }

                val rutina = innerNavController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Routine>("rutina")


                if (rutina != null) {
                    RutinaScreen(innerNavController, rutinaViewModel, rutina)
                }

            }
            composable(Favs.route) {

                val favsViewModel = remember { FavsViewModel(auth, db) }

                LaunchedEffect(Unit) {
                    favsViewModel.cargarEjerciciosFavs()
                }

                FavsScreen(navController = innerNavController, favsViewModel)

            }
        }
    }
}

@Composable
fun CabeceraOpcionesEjerciciosScreen(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.End
    ) {
        Spacer(Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_bench),
            contentDescription = "Logo aplicación",
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ExerciseHeaderOption(
                text = "Ejercicios",
                selected = currentRoute == Ejercicios.route,
                onClick = {
                    navController.navigate(Ejercicios.route) {
                        launchSingleTop = true
                    }
                }
            )

            ExerciseHeaderOption(
                text = "Rutinas",
                selected = currentRoute == Rutinas.route,
                onClick = {
                    navController.navigate(Rutinas.route) {
                        launchSingleTop = true
                    }
                }
            )

            ExerciseHeaderOption(
                text = "Favs",
                selected = currentRoute == "Favs",
                onClick = {
                    navController.navigate(Favs.route) {
                        launchSingleTop = true
                    }
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



