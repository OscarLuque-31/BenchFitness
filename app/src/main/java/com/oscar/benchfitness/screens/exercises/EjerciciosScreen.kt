package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel

@Composable
fun EjerciciosScreen(navController: NavController, viewModel: EjerciciosViewModel) {
    EjerciciosBodyContent(
        viewModel = viewModel
    )
}

@Composable
fun EjerciciosBodyContent(viewModel: EjerciciosViewModel) {

    Column {
        CabeceraOpcionesEjerciciosScreen()

    }
}


@Composable
fun CabeceraOpcionesEjerciciosScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Ejercicios",
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodySmall, fontSize = 24.sp,
                color = Color.White
            )
            Text(
                "Rutina",
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodySmall, fontSize = 24.sp,
                color = Color.White
            )
            Text(
                "Favs",
                modifier = Modifier.padding(bottom = 10.dp),
                style = MaterialTheme.typography.bodySmall, fontSize = 24.sp,
                color = Color.White
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

