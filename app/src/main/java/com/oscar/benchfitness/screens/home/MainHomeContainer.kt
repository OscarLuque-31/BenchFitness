package com.oscar.benchfitness.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.oscar.benchfitness.animations.LoadingScreen
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.navegation.CrearRutina
import com.oscar.benchfitness.navegation.Ejercicio
import com.oscar.benchfitness.navegation.Ejercicios
import com.oscar.benchfitness.navegation.Favs
import com.oscar.benchfitness.navegation.Goal
import com.oscar.benchfitness.navegation.Home
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
import com.oscar.benchfitness.viewModels.home.GoalViewModel
import com.oscar.benchfitness.viewModels.home.HomeViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainHomeContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val innerNavController = rememberNavController()

    Scaffold(containerColor = negroBench) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = Home.route,
            modifier = Modifier.fillMaxSize()

        ) {
            composable(Home.route) {
                val homeViewModel = remember { HomeViewModel(auth, db) }

                val isLoading by homeViewModel.isLoading.collectAsState()

                LaunchedEffect(Unit){
                    homeViewModel.cargarDatosUsuario()
                }

                if (isLoading) {
                    LoadingScreen()
                } else {
                    HomeScreen(
                        navController = innerNavController,
                        viewModel = homeViewModel,
                    )
                }
            }
            composable(Goal.route) {

                val user = innerNavController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<userData>("userData")

                if (user != null) {
                    val goalViewModel = remember { GoalViewModel(user) }
                    GoalScreen(user, goalViewModel, navController = innerNavController)
                }
            }
        }
    }
}