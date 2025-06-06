package com.oscar.benchfitness.screens.workout.exercises

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.animations.LoadingScreenCircularProgress
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.navegation.Ejercicio
import com.oscar.benchfitness.ui.theme.amarilloAvanzado
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.utils.colorPorNivel
import com.oscar.benchfitness.viewModels.workout.EjerciciosViewModel

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
        if (viewModel.isLoading) {
            LoadingScreenCircularProgress()
        } else {
            ListaEjercicios(navController, viewModel)
        }
    }
}

@Composable
fun FiltrosEjercicio(viewModel: EjerciciosViewModel) {

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
                text = "Filtros",
                backgroundColor = negroOscuroBench,
                colorText = Color.White,
                onClick = { viewModel.filtrosVisibles = !viewModel.filtrosVisibles },
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
                backgroundColor = negroOscuroBench,
                onDone = {
                    // Filtra el ejercicio por nombre
                    viewModel.filtrarBusquedaNombre(viewModel.busqueda)
                },
                imeAction = ImeAction.Search
            )
        }

        // Diálogo con filtros si está activo
        InfoDialog(
            title = "Filtros de ejercicio",
            showDialog = viewModel.filtrosVisibles,
            onDismiss = { viewModel.filtrosVisibles = false },
            cuerpo = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GlobalDropDownMenu(
                        nombreSeleccion = viewModel.musculo,
                        opciones = musculos.map { it.nombre },
                        onValueChange = { viewModel.musculo = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        backgroundColor = negroOscuroBench,
                        colorText = rojoBench
                    )
                    Spacer(Modifier.height(20.dp))
                    GlobalDropDownMenu(
                        nombreSeleccion = viewModel.categoria,
                        opciones = categorias.map { it.nombre },
                        onValueChange = { viewModel.categoria = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        backgroundColor = negroOscuroBench,
                        colorText = rojoBench

                    )
                    Spacer(Modifier.height(20.dp))
                    GlobalDropDownMenu(
                        nombreSeleccion = viewModel.nivel,
                        opciones = niveles.map { it.nombre },
                        onValueChange = { viewModel.nivel = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        backgroundColor = negroOscuroBench,
                        colorText = rojoBench
                    )
                    Spacer(Modifier.height(20.dp))
                    GlobalButton(
                        text = "Aplicar búsqueda",
                        onClick = {
                            // Aplica la búsqueda y retorna los datos
                            viewModel.onClickBuscar()
                            viewModel.filtrosVisibles = false
                        },
                        backgroundColor = rojoBench,
                        colorText = negroOscuroBench,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))
                    GlobalButton(
                        text = "Resetear filtros",
                        onClick = {
                            // Carga los ejercicios desde 0 sin filtros
                            viewModel.cargarEjercicios()
                            viewModel.filtrosVisibles = false
                        },
                        backgroundColor = negroOscuroBench,
                        colorText = rojoBench,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}

@Composable
fun ListaEjercicios(navController: NavController, viewModel: EjerciciosViewModel) {
    val ejercicios by viewModel.ejercicios.collectAsState()

    // Si no se encuentran ejercicios con el filtro aparece este mensaje
    if (ejercicios.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "No se encontraron ejercicios",
                fontSize = 20.sp,
                color = rojoBench,
                fontWeight = FontWeight.Medium
            )
        }
        // Si aparecen se mostrarán todos los ejercicios del filtro
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(ejercicios) { ejercicio ->
                CajaEjercicio(navController, ejercicio)
            }
        }
    }
}

@Composable
fun CajaEjercicio(navController: NavController, ejercicio: ExerciseData) {
    Column(
        modifier = Modifier
            .height(150.dp)
            .padding(bottom = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                // Envia el ejercicio por el controlador de navegación
                navController.currentBackStackEntry?.savedStateHandle?.set("ejercicio", ejercicio)
                // Navega al ejercicio para ver sus detalles
                navController.navigate(Ejercicio.route)
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
                // Según el nivel tendra un color u otro
                color = colorPorNivel(ejercicio.nivel)

            )
        }
    }
}
