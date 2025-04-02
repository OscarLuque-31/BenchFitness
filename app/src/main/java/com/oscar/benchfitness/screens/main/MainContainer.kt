package com.oscar.benchfitness.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.animations.LoadingScreen
import com.oscar.benchfitness.components.GlobalBarraNavegacion
import com.oscar.benchfitness.navegation.Ejercicios
import com.oscar.benchfitness.navegation.Estadisticas
import com.oscar.benchfitness.navegation.Home
import com.oscar.benchfitness.navegation.Perfil
import com.oscar.benchfitness.screens.exercises.EjerciciosScreen
import com.oscar.benchfitness.screens.home.HomeScreen
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel
import com.oscar.benchfitness.viewModels.home.HomeViewModel

@Composable
fun MainContainer(navController: NavController, auth: FirebaseAuth, db: FirebaseFirestore) {

    val innerNavController = rememberNavController()

    Scaffold(containerColor = negroBench, bottomBar = {
        Box(
            modifier = Modifier
                .padding(vertical = 15.dp)
        ) {
            GlobalBarraNavegacion(innerNavController)
        }
    }) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Home> {

                val homeViewModel = remember { HomeViewModel(auth, db) }

                LaunchedEffect(Unit) {
                    homeViewModel.cargarDatosUsuario()
                }

                val isLoading by homeViewModel.isLoading.collectAsState()

                if (isLoading) {
                    LoadingScreen()
                } else {
                    HomeScreen(
                        navController = innerNavController,
                        viewModel = homeViewModel,
                    )
                }

            }
            composable<Ejercicios> {
                val ejerciciosViewModel = remember { EjerciciosViewModel(auth, db) }

                EjerciciosScreen(
                    navController = innerNavController,
                    viewModel = ejerciciosViewModel,
                )
            }
            composable<Estadisticas> {

            }
            composable<Perfil> {

            }
        }
    }

}