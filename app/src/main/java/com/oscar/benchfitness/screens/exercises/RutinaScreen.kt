package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.exercises.RutinaViewModel

@Composable
fun RutinaScreen(navController: NavController, viewModel: RutinaViewModel, rutina: Routine) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(20.dp))
                .background(negroOscuroBench)
        ) {
            ColumnaDatosRutina(rutina, navController, viewModel)
        }
    }
}

@Composable
fun ColumnaDatosRutina(rutina: Routine, navController: NavController, viewModel: RutinaViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        FlechitaAtras(navController)
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(rutina.nombre, color = rojoBench, fontSize = 30.sp, fontWeight = FontWeight.Normal)
        }
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.objetivo),
                contentDescription = "Objetivo",
                modifier = Modifier.size(25.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                rutina.objetivo,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.fecha),
                contentDescription = "Objetivo",
                modifier = Modifier.size(25.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                rutina.fechaCreacion.replace(oldChar = '-', newChar = '/'),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(20.dp))
        DatosRutina(rutina)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            GlobalButton("Borrar rutina",
                backgroundColor = rojoBench,
                colorText = negroOscuroBench,
                modifier = Modifier,
                onClick = {
                    showDialog = true
                })
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    // Usamos tus propios botones aquí
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GlobalButton(
                            text = "Cancelar",
                            backgroundColor = negroClaroBench,
                            colorText = Color.White,
                            modifier = Modifier,
                            onClick = { showDialog = false }
                        )
                        GlobalButton(
                            text = "Eliminar",
                            backgroundColor = rojoBench,
                            colorText = negroOscuroBench,
                            onClick = {
                                viewModel.eliminarRutina(rutina.nombre)
                                showDialog = false
                                navController.popBackStack()
                            },
                            modifier = Modifier
                        )
                    }
                },
                title = {
                    Text(
                        text = "Eliminar rutina",
                        color = rojoBench,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que quieres eliminar esta rutina? Esta acción no se puede deshacer.",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                },
                containerColor = negroOscuroBench,
                shape = RoundedCornerShape(20.dp)
            )
        }


    }
}

@Composable
fun DatosRutina(rutina: Routine) {

    val ordenDiasSemana =
        listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    rutina.dias
        .sortedBy { dia -> ordenDiasSemana.indexOf(dia.dia) }
        .forEach { dia ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(negroBench)
            ) {
                Text(
                    dia.dia,
                    color = rojoBench,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 15.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroBench)
            ) {
                Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {

                    Spacer(Modifier.height(10.dp))
                    EncabezadoNombreSerieReps(negroOscuroBench)
                    dia.ejercicios.forEach { ejercicio ->
                        BoxExerciseEntry(
                            nombre = ejercicio.nombre,
                            series = ejercicio.series.toString(),
                            repeticiones = ejercicio.repeticiones.toString(),
                            negroOscuroBench,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
}