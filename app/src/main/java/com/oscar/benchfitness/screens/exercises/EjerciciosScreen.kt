package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.navegation.Ejercicio
import com.oscar.benchfitness.ui.theme.amarilloAvanzado
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.viewModels.exercises.EjerciciosViewModel

@Composable
fun EjerciciosScreen(navController: NavController, viewModel: EjerciciosViewModel) {
    EjerciciosBodyContent(
        navController,
        viewModel = viewModel
    )
}

@Composable
fun EjerciciosBodyContent(navController: NavController, viewModel: EjerciciosViewModel) {


    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        FiltrosEjercicio(viewModel)
        ListaEjercicios(navController,viewModel)
    }
}

@Composable
fun FiltrosEjercicio(viewModel: EjerciciosViewModel) {
    var filtrosVisibles by remember { mutableStateOf(false) }

    val musculos by viewModel.musculos.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val niveles by viewModel.niveles.collectAsState()

    Column(
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlobalButton(
                text = if (filtrosVisibles) "Filtros ↑" else "Filtros ↓",
                backgroundColor = negroOscuroBench,
                colorText = Color.White,
                onClick = { filtrosVisibles = !filtrosVisibles },
                modifier = Modifier
                    .height(50.dp)
                    .weight(0.3f)
            )
            Spacer(Modifier.width(30.dp))
            GlobalTextField(
                nombre = "Buscar...",
                isPassword = false,
                text = viewModel.busqueda,
                onValueChange = { viewModel.busqueda = it },
                modifier = Modifier
                    .weight(0.5f)
                    .height(50.dp),
                colorText = Color.White,
                backgroundColor = negroOscuroBench
            )
        }

        if (filtrosVisibles) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(negroOscuroBench, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    GlobalDropDownMenu(
                        nombreSeleccion = viewModel.musculo,
                        opciones = musculos.map { it.nombre },
                        onValueChange = { viewModel.musculo = it },
                        modifier = Modifier
                            .width(250.dp)
                            .height(50.dp),
                        backgroundColor = rojoBench
                    )
                    Spacer(Modifier.height(20.dp))
                    GlobalDropDownMenu(
                        nombreSeleccion = viewModel.categoria,
                        opciones = categorias.map { it.nombre },
                        onValueChange = { viewModel.categoria = it },
                        modifier = Modifier
                            .width(250.dp)
                            .height(50.dp),
                        backgroundColor = rojoBench
                    )
                    Spacer(Modifier.height(20.dp))
                    GlobalDropDownMenu(
                        nombreSeleccion = viewModel.nivel,
                        opciones = niveles.map { it.nombre },
                        onValueChange = { viewModel.nivel = it },
                        modifier = Modifier
                            .width(250.dp)
                            .height(50.dp),
                        backgroundColor = rojoBench
                    )
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "buscar",
                            tint = rojoBench,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ListaEjercicios(navController: NavController, viewModel: EjerciciosViewModel) {
    val ejercicios by viewModel.ejercicios.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(ejercicios) { ejercicio ->
            CajaEjercicio(navController,ejercicio)
        }
    }
}

@Composable
fun CajaEjercicio(navController: NavController,ejercicio: ExerciseData) {

    Column(
        modifier = Modifier
            .height(150.dp)
            .padding(bottom = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("ejercicio", ejercicio)
                navController.navigate(Ejercicio)
            }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
                .background(negroOscuroBench)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = ejercicio.nombre,
                fontSize = 19.sp,
                color = rojoBench
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35f)
                .background(negroClaroBench),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = ejercicio.musculo_principal,
                fontSize = 15.sp,
                color = Color.White

            )
            Text(
                text = ejercicio.categoria,
                fontSize = 15.sp,
                color = Color.White

            )
            Text(
                text = ejercicio.nivel,
                fontSize = 15.sp,
                color = colorPorNivel(ejercicio.nivel)

            )
        }
    }

}

fun colorPorNivel(nivel: String): Color {
    return when (nivel) {
        "Principiante" -> verdePrincipiante
        "Intermedio" -> azulIntermedio
        "Avanzado" -> amarilloAvanzado
        else -> Color.White
    }
}
