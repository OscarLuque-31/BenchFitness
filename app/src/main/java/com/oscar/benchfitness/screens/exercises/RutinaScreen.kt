package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.models.ExerciseData
import com.oscar.benchfitness.models.ExerciseRoutineEntry
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.utils.interpretarDia
import com.oscar.benchfitness.viewModels.exercises.RutinaViewModel

@Composable
fun RutinaScreen(navController: NavController, viewModel: RutinaViewModel) {

    LaunchedEffect(Unit) {
        viewModel.obtenerNombreEjercicios()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Habilita el scroll
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                negroOscuroBench
            )
    ) {
        ColumnaCreacionRutina(navController, viewModel)
    }
}


@Composable
fun ColumnaCreacionRutina(navController: NavController, viewModel: RutinaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        FlechitaAtras(navController = navController)
        Spacer(Modifier.height(20.dp))
        GlobalTextField(
            nombre = "Nombre de la rutina...",
            text = viewModel.nombreRutina,
            colorText = rojoBench,
            imeAction = ImeAction.Default,
            backgroundColor = negroBench,
            onValueChange = { viewModel.nombreRutina = it },
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Spacer(Modifier.height(20.dp))
        FilaObjetivoRutina(viewModel)
        Spacer(Modifier.height(20.dp))
        FilaBotonesDias(viewModel)
        Spacer(Modifier.height(20.dp))
        FilaDropdownDias(viewModel)
        Spacer(Modifier.height(20.dp))
        BoxAnadirEjerciciosPorDia(viewModel)

        if (viewModel.showDialog) {
            DialogAddExercise(
                onDismiss = { viewModel.showDialog = false },
                onAdd = { ejercicioRoutineEntry ->
                    viewModel.agregarEjercicioAlDia(
                        dia = viewModel.diaSeleccionado,
                        entry = ExerciseRoutineEntry(nombre = ejercicioRoutineEntry.nombre,
                            series = ejercicioRoutineEntry.series,
                            repeticiones = ejercicioRoutineEntry.repeticiones)
                    )
                    viewModel.showDialog = false
                },
                viewModel = viewModel // Pasar el viewModel al diálogo
            )
        }
    }
}


@Composable
fun FilaObjetivoRutina(viewModel: RutinaViewModel) {

    val objetivos: List<String> = listOf("Hipertrofia", "Fuerza")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.objetivo),
            contentDescription = "Objetivo",
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.width(20.dp))
        GlobalDropDownMenu(
            nombreSeleccion = viewModel.objetivo,
            onValueChange = { viewModel.objetivo = it },
            opciones = objetivos,
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            backgroundColor = negroBench,
            colorText = rojoBench
        )
    }
}

@Composable
fun FilaBotonesDias(viewModel: RutinaViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        BotonDia("L", viewModel)
        BotonDia("M", viewModel)
        BotonDia("X", viewModel)
        BotonDia("J", viewModel)
        BotonDia("V", viewModel)
        BotonDia("S", viewModel)
        BotonDia("D", viewModel)
    }
}

@Composable
fun BotonDia(dia: String, viewModel: RutinaViewModel) {
    val pulsado = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .height(35.dp)
            .width(35.dp)
            .clip(RoundedCornerShape(100.dp))
            .border(1.dp, if (pulsado.value) negroBench else rojoBench, RoundedCornerShape(100.dp))
            .background(if (pulsado.value) rojoBench else negroBench)
            .clickable {
                pulsado.value = !pulsado.value

                val diaCompleto = interpretarDia(dia)

                if (pulsado.value) {
                    viewModel.diasSeleccionados += diaCompleto

                    // Seleccionarlo en el dropdown si no hay nada seleccionado o si no coincide con el día tocado
                    if (viewModel.diaSeleccionado.isBlank()) {
                        viewModel.diaSeleccionado = diaCompleto
                    }
                } else {
                    viewModel.diasSeleccionados -= diaCompleto

                    // Si se deselecciona el día que estaba en el dropdown, quitarlo del dropdown también
                    if (viewModel.diaSeleccionado == diaCompleto) {
                        // Aquí buscamos otro día si es posible
                        if (viewModel.diasSeleccionados.isNotEmpty()) {
                            // Establecer el primer día de la lista seleccionada, si la lista no está vacía
                            viewModel.diaSeleccionado = viewModel.diasSeleccionados.first()
                        } else {
                            // Si no hay días seleccionados, dejamos el valor en blanco
                            viewModel.diaSeleccionado = ""
                        }
                    }
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            dia,
            color = if (pulsado.value) negroOscuroBench else rojoBench,
            fontWeight = FontWeight.W300,
            fontSize = 18.sp
        )
    }
}


@Composable
fun FilaDropdownDias(viewModel: RutinaViewModel) {
    // Verifica si hay días seleccionados
    if (viewModel.diasSeleccionados.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.fecha),
                contentDescription = "fecha",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(20.dp))
            GlobalDropDownMenu(
                nombreSeleccion = viewModel.diaSeleccionado,
                onValueChange = { viewModel.diaSeleccionado = it },
                opciones = viewModel.diasSeleccionados,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                backgroundColor = negroBench,
                colorText = rojoBench
            )
        }
    } else {
        // Si no hay días seleccionados, no mostrar el Dropdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Selecciona al menos un día",
                color = rojoBench,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun BoxAnadirEjerciciosPorDia(viewModel: RutinaViewModel) {
    if (viewModel.diasSeleccionados.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(negroBench),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ListaEjerciciosPorDia(
                viewModel, viewModel.diaSeleccionado,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                GlobalButton(
                    colorText = rojoBench,
                    text = "Añadir",
                    backgroundColor = negroOscuroBench,
                    modifier = Modifier
                        .padding(10.dp)
                        .height(40.dp)
                ) {
                    viewModel.showDialog = !viewModel.showDialog
                }
            }
        }
    }
}

@Composable
fun ListaEjerciciosPorDia(viewModel: RutinaViewModel, dia: String) {
    val ejerciciosList = viewModel.ejerciciosPorDia[dia] ?: emptyList()
    val ejercicioSeleccionado = remember { mutableStateOf<ExerciseRoutineEntry?>(null) }

    if (ejerciciosList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(negroOscuroBench)
                    .padding(vertical = 6.dp, horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.CenterStart) {
                    Text("Nombre", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                    Text("Series", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                    Text("Reps", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(10.dp))

            // Lista de ejercicios
            LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                items(ejerciciosList) { ejercicio ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (ejercicioSeleccionado.value == ejercicio)
                                    rojoBench.copy(alpha = 0.2f)
                                else negroOscuroBench
                            )
                            .clickable {
                                ejercicioSeleccionado.value = ejercicio
                            }
                            .padding(vertical = 6.dp, horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(0.5f), contentAlignment = Alignment.CenterStart) {
                            Text(
                                ejercicio.nombre,
                                color = rojoBench,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                            Text(ejercicio.series.toString(), color = verdePrincipiante, fontSize = 15.sp)
                        }
                        Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                            Text(ejercicio.repeticiones.toString(), color = azulIntermedio, fontSize = 15.sp)
                        }
                    }

                    Spacer(Modifier.height(10.dp))
                }
            }

            // Botón eliminar si hay seleccionado
            if (ejercicioSeleccionado.value != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    GlobalButton(
                        text = "Eliminar",
                        colorText = rojoBench,
                        backgroundColor = negroOscuroBench,
                        modifier = Modifier
                    ) {
                        viewModel.eliminarEjercicioDelDia(viewModel.diaSeleccionado, entry = ejercicioSeleccionado.value!!)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(130.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "No has añadido ejercicios",
                color = rojoBench,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }
    }
}



@Composable
fun DialogAddExercise(
    onDismiss: () -> Unit,
    onAdd: (ExerciseRoutineEntry) -> Unit,
    viewModel: RutinaViewModel
) {
    val errorMessage = remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = negroOscuroBench,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Añadir Ejercicio",
                    color = rojoBench,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                GlobalDropDownMenu(
                    nombreSeleccion = viewModel.ejercicioSeleccionado,
                    onValueChange = { viewModel.ejercicioSeleccionado = it },
                    opciones = viewModel.listaEjercicios,
                    modifier = Modifier.width(240.dp),
                    backgroundColor = negroBench,
                    colorText = rojoBench
                )

                Spacer(modifier = Modifier.height(10.dp))

                GlobalTextField(
                    nombre = "Series",
                    text = viewModel.series.toString(),
                    colorText = rojoBench,
                    onValueChange = { viewModel.series = it.toIntOrNull() ?: 0 },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = negroBench
                )

                Spacer(modifier = Modifier.height(10.dp))

                GlobalTextField(
                    nombre = "Repeticiones",
                    text = viewModel.repeticiones.toString(),
                    colorText = rojoBench,
                    onValueChange = { viewModel.repeticiones = it.toIntOrNull() ?: 0 },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = negroBench
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (errorMessage.value.isNotEmpty()) {
                    Text(
                        text = errorMessage.value,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GlobalButton(
                        text = "Cancelar",
                        colorText = rojoBench,
                        backgroundColor = negroBench,
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = { onDismiss() }
                    )

                    GlobalButton(
                        text = "Añadir",
                        colorText = rojoBench,
                        backgroundColor = negroBench,
                        onClick = {
                            val seriesValid = viewModel.series
                            val repeticionesValid = viewModel.repeticiones

                            if (viewModel.ejercicioSeleccionado.isBlank()) {
                                errorMessage.value = "Selecciona un ejercicio."
                            } else if (seriesValid == null || seriesValid <= 0) {
                                errorMessage.value = "Introduce un número válido de series."
                            } else if (repeticionesValid == null || repeticionesValid <= 0) {
                                errorMessage.value = "Introduce un número válido de repeticiones."
                            } else {
                                errorMessage.value = ""
                                val ejercicio = ExerciseRoutineEntry(nombre = viewModel.ejercicioSeleccionado,
                                    series = viewModel.series,
                                    repeticiones = viewModel.repeticiones)
                                onAdd(ejercicio)
                                viewModel.resetFormularioEjercicio()
                            }
                        },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}



