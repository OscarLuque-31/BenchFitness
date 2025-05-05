package com.oscar.benchfitness.screens.statistics

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.navegation.Deficit
import com.oscar.benchfitness.navegation.Estadisticas
import com.oscar.benchfitness.navegation.Metabolismo
import com.oscar.benchfitness.navegation.Peso
import com.oscar.benchfitness.navegation.Progreso
import com.oscar.benchfitness.navegation.RepeMaxima
import com.oscar.benchfitness.navegation.Superavit
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.viewModels.statistics.CalculosViewModel
import com.oscar.benchfitness.viewModels.statistics.PesoViewModel
import com.oscar.benchfitness.viewModels.statistics.ProgresoViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainStatisticsContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val innerNavController = rememberNavController()

    Scaffold(containerColor = negroBench) {
        NavHost(
            navController = innerNavController,
            startDestination = Estadisticas.route,
            modifier = Modifier // sin padding
        ) {
            composable(Estadisticas.route) {
                StatisticsScreen(navController = innerNavController)
            }
            composable(Peso.route) {
                val pesoViewModel = remember { PesoViewModel(auth, db) }
                PesoScreen(viewModel = pesoViewModel, navController = innerNavController)
            }
            composable(Progreso.route) {
                val progresoViewModel = remember { ProgresoViewModel(auth, db) }

                ProgresoScreen(viewModel = progresoViewModel, navController = innerNavController)
            }
            composable(Metabolismo.route) {
                val calculosViewModel = remember { CalculosViewModel(auth, db) }

                MetabolismoScreen(viewModel = calculosViewModel, navController = innerNavController)
            }
            composable(Deficit.route) {
                val calculosViewModel = remember { CalculosViewModel(auth, db) }

                DeficitScreen(viewModel = calculosViewModel, navController = innerNavController)
            }
            composable(RepeMaxima.route) {
                val calculosViewModel = remember { CalculosViewModel(auth, db) }

                RepeMaximaScreen(viewModel = calculosViewModel, navController = innerNavController)
            }
            composable(Superavit.route) {
                val calculosViewModel = remember { CalculosViewModel(auth, db) }

                SuperavitScreen(viewModel = calculosViewModel, navController = innerNavController)
            }
        }
    }
}
