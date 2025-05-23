package com.oscar.benchfitness.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.animations.LoadingScreenCircularProgress
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.navegation.Goal
import com.oscar.benchfitness.navegation.Home
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.viewModels.home.GoalViewModel
import com.oscar.benchfitness.viewModels.home.HomeViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainHomeContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore
) {
    val innerNavController = rememberNavController()
    val homeViewModel = remember { HomeViewModel(auth, db) }
    val user = homeViewModel.userData
    val isLoading = homeViewModel.isLoading

    LaunchedEffect(Unit) {
        homeViewModel.cargarDatosUsuario()
    }

    Scaffold(containerColor = negroBench, topBar = {
        GlobalHeader("Bienvenido ${user.username}")
    }) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = Home.route,
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
            )

        ) {
            composable(Home.route) {
                if (isLoading) {
                    LoadingScreenCircularProgress()
                } else {
                    HomeScreen(
                        navController = innerNavController,
                        viewModel = homeViewModel,
                    )
                }
            }
            composable(Goal.route) {

                if (user != null) {
                    val goalViewModel = remember { GoalViewModel(user) }
                    GoalScreen(user, goalViewModel, navController = innerNavController)
                }
            }
        }
    }
}