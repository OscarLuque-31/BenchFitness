package com.oscar.benchfitness.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.FormularioCalorias
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.viewModels.statistics.CalculosViewModel

@Composable
fun MetabolismoScreen(navController: NavController, viewModel: CalculosViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GlobalHeader("Metabolismo")
        ColumnaMetabolismo(navController, viewModel)
    }
}

@Composable
fun ColumnaMetabolismo(navController: NavController, viewModel: CalculosViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            FlechitaAtras(navController = navController)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Calculadora mantener",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            FormularioCalorias(
                viewModel = viewModel,
                // Calcula las calorías para mantener
                onClick = { viewModel.calcularCaloriasMantener() }
            )
        }
    }
}
