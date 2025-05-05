package com.oscar.benchfitness.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.components.GlobalBarraNavegacion
import com.oscar.benchfitness.navegation.Estadisticas
import com.oscar.benchfitness.navegation.Home
import com.oscar.benchfitness.navegation.MainExercises
import com.oscar.benchfitness.navegation.MainHome
import com.oscar.benchfitness.navegation.MainStatistics
import com.oscar.benchfitness.navegation.Perfil
import com.oscar.benchfitness.screens.exercises.MainExercisesContainer
import com.oscar.benchfitness.screens.home.MainHomeContainer
import com.oscar.benchfitness.screens.statistics.MainStatisticsContainer
import com.oscar.benchfitness.ui.theme.negroBench

@Composable
fun MainContainer(auth: FirebaseAuth, db: FirebaseFirestore) {

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
            composable(MainExercises.route) {
                MainExercisesContainer(auth, db)
            }
            composable(MainStatistics.route) {
                MainStatisticsContainer(auth, db)
            }
            composable(Perfil.route) {

            }

        }
    }
}