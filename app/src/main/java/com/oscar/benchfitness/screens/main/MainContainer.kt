package com.oscar.benchfitness.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.components.GlobalBarraNavegacion
import com.oscar.benchfitness.navegation.MainHome
import com.oscar.benchfitness.navegation.MainPerfil
import com.oscar.benchfitness.navegation.MainStatistics
import com.oscar.benchfitness.navegation.MainWorkout
import com.oscar.benchfitness.screens.home.MainHomeContainer
import com.oscar.benchfitness.screens.profile.MainPerfilContainer
import com.oscar.benchfitness.screens.statistics.MainStatisticsContainer
import com.oscar.benchfitness.screens.workout.MainWorkoutContainer
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.viewModels.auth.AuthViewModel

@Composable
fun MainContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    // Controlador principal de la navegación
    val innerNavController = rememberNavController()

    Scaffold(containerColor = negroBench, bottomBar = {
        Box(
            modifier = Modifier
                .padding(bottom = 25.dp, top = 15.dp)
        ) {
            GlobalBarraNavegacion(innerNavController)
        }
    }) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = MainHome.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MainHome.route) {
                MainHomeContainer(auth, db)
            }
            composable(MainWorkout.route) {
                MainWorkoutContainer(auth, db)
            }
            composable(MainStatistics.route) {
                MainStatisticsContainer(auth, db)
            }
            composable(MainPerfil.route) {
                MainPerfilContainer(auth, db, navController, authViewModel)
            }

        }
    }
}