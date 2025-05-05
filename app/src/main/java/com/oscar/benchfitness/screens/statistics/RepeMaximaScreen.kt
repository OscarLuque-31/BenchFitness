package com.oscar.benchfitness.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.viewModels.statistics.CalculosViewModel
import com.oscar.benchfitness.viewModels.statistics.PesoViewModel

@Composable
fun RepeMaximaScreen(navController: NavController, viewModel: CalculosViewModel) {

    Column(modifier = Modifier.fillMaxWidth()) {
        GlobalHeader("Repetición Máxima")
        ColumnaRepeMaxima(navController, viewModel)
    }

}

@Composable
fun ColumnaRepeMaxima(navController: NavController, viewModel: CalculosViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)) {
            FlechitaAtras(navController = navController)

        }


    }
}
