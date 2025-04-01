package com.oscar.benchfitness.screens.exercises

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel

@Composable
fun EjerciciosScreen(navController: NavController, viewModel: EjerciciosViewModel) {

    EjerciciosBodyContent(
        viewModel = viewModel
    )

}

@Composable
fun EjerciciosBodyContent(viewModel: Any) {

}


