package com.oscar.benchfitness.screens.workout

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
import androidx.compose.runtime.getValue
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
import com.oscar.benchfitness.navegation.MainExercises
import com.oscar.benchfitness.navegation.MainFavs
import com.oscar.benchfitness.navegation.MainRoutines
import com.oscar.benchfitness.screens.workout.exercises.MainExercisesContainer
import com.oscar.benchfitness.screens.workout.favs.MainFavsContainer
import com.oscar.benchfitness.screens.workout.routines.MainRoutinesContainer
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench

@Composable
fun MainWorkoutContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    // Controlador principal de la sección de Workout
    val innerNavController = rememberNavController()

    Scaffold(containerColor = negroBench, topBar = {
        CabeceraOpcionesEjerciciosScreen(innerNavController)
    }) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = MainExercises.route,
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = 0.dp
            )
        ) {
            composable(MainExercises.route) {
                MainExercisesContainer(auth, db)
            }

            composable(MainRoutines.route) {
                MainRoutinesContainer(auth, db)
            }

            composable(MainFavs.route) {
                MainFavsContainer(auth, db)
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
                // Si la actual ruta es igual a la ruta que voy a viajar se cambia el color a rojo
                selected = currentRoute == MainExercises.route,
                onClick = {
                    navController.navigate(MainExercises.route) {
                        launchSingleTop = true
                    }
                }
            )
            ExerciseHeaderOption(
                text = "Rutinas",
                selected = currentRoute == MainRoutines.route,
                onClick = {
                    navController.navigate(MainRoutines.route) {
                        launchSingleTop = true
                    }
                }
            )
            ExerciseHeaderOption(
                text = "Favs",
                selected = currentRoute == MainFavs.route,
                onClick = {
                    navController.navigate(MainFavs.route) {
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



