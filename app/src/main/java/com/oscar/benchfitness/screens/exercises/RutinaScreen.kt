package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.navegation.Rutinas
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.viewModels.exercises.RutinaViewModel

@Composable
fun RutinaScreen(navController: NavController, viewModel: RutinaViewModel, rutina: Routine) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ColumnaDatosRutina(rutina, navController)
    }
}

@Composable
fun ColumnaDatosRutina(rutina: Routine, navController: NavController){
    Column (modifier = Modifier.fillMaxSize().background(negroOscuroBench)) {
        FlechitaAtras(navController)
        Column (modifier = Modifier.fillMaxSize().background(negroOscuroBench)){
            Text(rutina.nombre)
            Text(rutina.objetivo)
            Text(rutina.fechaCreacion)
            rutina.dias.forEach { dia ->
                Column (modifier = Modifier.background(negroBench)) {
                    Text(dia.dia)
                    EncabezadoNombreSerieReps()
                    dia.ejercicios.forEach { ejercicio ->
                        BoxExerciseEntry(nombre = ejercicio.nombre,
                            series = ejercicio.series.toString(),
                            repeticiones = ejercicio.repeticiones.toString())
                    }
                }

            }
        }
    }
}