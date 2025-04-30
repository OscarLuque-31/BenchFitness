package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel
import com.oscar.benchfitness.viewModels.exercises.FavsViewModel

@Composable
fun FavsScreen(navController: NavController, viewModel: FavsViewModel) {
    ListaEjerciciosFavoritos(navController , viewModel)
}

@Composable
fun ListaEjerciciosFavoritos(navController: NavController, viewModel: FavsViewModel) {
    val ejercicios by viewModel.ejercicios.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 10.dp)) {
        items(ejercicios) { ejercicio ->
            CajaEjercicio(navController, ejercicio)
        }
    }
}