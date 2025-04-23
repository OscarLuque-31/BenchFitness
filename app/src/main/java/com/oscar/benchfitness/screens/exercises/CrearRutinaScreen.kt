package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.models.ExerciseRoutineEntry
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.utils.interpretarDia
import com.oscar.benchfitness.viewModels.exercises.CrearRutinaViewModel

@Composable
fun CrearRutinaScreen(navController: NavController, viewModel: CrearRutinaViewModel) {
    LaunchedEffect(Unit) {
        viewModel.obtenerNombreEjercicios()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.ejercicioSeleccionado = null
            }
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(negroOscuroBench)
        ) {
            ColumnaCreacionRutina(navController, viewModel)
        }
    }
}


@Composable
fun ColumnaCreacionRutina(navController: NavController, viewModel: CrearRutinaViewModel) {
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
        Spacer(Modifier.height(20.dp))
        BotonCrearRutina(viewModel)

        if (viewModel.showDialog) {
            DialogAddExercise(
                onDismiss = { viewModel.showDialog = false },
                onAdd = { ejercicioRoutineEntry ->
                    viewModel.agregarEjercicioAlDia(
                        dia = viewModel.diaSeleccionado,
                        entry = ExerciseRoutineEntry(
                            nombre = ejercicioRoutineEntry.nombre,
                            series = ejercicioRoutineEntry.series,
                            repeticiones = ejercicioRoutineEntry.repeticiones
                        )
                    )
                    viewModel.showDialog = false
                },
                viewModel = viewModel // Pasar el viewModel al diálogo
            )
        }
    }
}


@Composable
fun FilaObjetivoRutina(viewModel: CrearRutinaViewModel) {

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
fun FilaBotonesDias(viewModel: CrearRutinaViewModel) {
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
fun BotonDia(dia: String, viewModel: CrearRutinaViewModel) {
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
fun FilaDropdownDias(viewModel: CrearRutinaViewModel) {
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
fun BoxAnadirEjerciciosPorDia(viewModel: CrearRutinaViewModel) {
    if (viewModel.diasSeleccionados.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .background(negroBench)
        ) {
            val ejerciciosList =
                viewModel.ejerciciosPorDia[viewModel.diaSeleccionado] ?: emptyList()

            if (ejerciciosList.isNotEmpty()) {
                ListaEjerciciosPorDia(viewModel, viewModel.diaSeleccionado)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ejerciciosList.isEmpty()) {
                    Text(
                        "Añade ejercicios",
                        color = rojoBench,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                if (viewModel.ejercicioSeleccionado != null && ejerciciosList.isNotEmpty()) {
                    GlobalButton(
                        text = "Eliminar",
                        colorText = negroOscuroBench,
                        backgroundColor = rojoBench,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        viewModel.eliminarEjercicioDelDia(
                            viewModel.diaSeleccionado,
                            entry = viewModel.ejercicioSeleccionado!!
                        )
                    }
                }

                GlobalButton(
                    colorText = rojoBench,
                    text = "Añadir",
                    backgroundColor = negroOscuroBench,
                    modifier = Modifier.padding(10.dp)
                ) {
                    viewModel.showDialog = !viewModel.showDialog
                }
            }
        }
    }
}

@Composable
fun ListaEjerciciosPorDia(viewModel: CrearRutinaViewModel, dia: String) {
    val ejerciciosList = viewModel.ejerciciosPorDia[dia] ?: emptyList()

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
                Text(
                    "Nombre",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                Text(
                    "Series",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(modifier = Modifier.weight(0.25f), contentAlignment = Alignment.Center) {
                Text(
                    "Reps",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Lista de ejercicios
        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
            items(ejerciciosList) { ejercicio ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (viewModel.ejercicioSeleccionado == ejercicio)
                                rojoBench.copy(alpha = 0.2f)
                            else negroOscuroBench
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.ejercicioSeleccionado =
                                if (viewModel.ejercicioSeleccionado == ejercicio) null
                                else ejercicio
                        }
                        .padding(vertical = 6.dp, horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(0.5f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            ejercicio.nombre,
                            color = rojoBench,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Box(
                        modifier = Modifier.weight(0.25f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            ejercicio.series.toString(),
                            color = verdePrincipiante,
                            fontSize = 15.sp
                        )
                    }
                    Box(
                        modifier = Modifier.weight(0.25f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            ejercicio.repeticiones.toString(),
                            color = azulIntermedio,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}


@Composable
fun DialogAddExercise(
    onDismiss: () -> Unit,
    onAdd: (ExerciseRoutineEntry) -> Unit,
    viewModel: CrearRutinaViewModel
) {
    val errorMessage = remember { mutableStateOf("") }
    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            viewModel.nombreEjercicioSeleccionado = ""
        }
    }

    val filteredExercises = remember(searchQuery.value, viewModel.listaEjercicios) {
        if (searchQuery.value.isEmpty()) viewModel.listaEjercicios
        else viewModel.listaEjercicios.filter { it.contains(searchQuery.value, ignoreCase = true) }
    }

    val dynamicHeight =
        when (viewModel.nombreEjercicioSeleccionado.isNotEmpty() || filteredExercises.isEmpty()) {
            true -> 0.6f
            false -> 0.9f
        }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = negroOscuroBench,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(dynamicHeight)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Añadir Ejercicio",
                    color = rojoBench,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ExerciseSearchField(searchQuery)

                ExerciseListOrSelected(
                    viewModel = viewModel,
                    filteredExercises = filteredExercises
                )

                Spacer(modifier = Modifier.height(16.dp))

                SeriesRepsFields(viewModel)

                ErrorMessage(errorMessage)

                ActionButtons(
                    onDismiss = onDismiss,
                    onAdd = onAdd,
                    viewModel = viewModel,
                    errorMessage = errorMessage
                )
            }
        }
    }
}

@Composable
fun ExerciseSearchField(searchQuery: MutableState<String>) {
    GlobalTextField(
        nombre = "Buscar ejercicio...",
        text = searchQuery.value,
        colorText = rojoBench,
        onValueChange = { searchQuery.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        backgroundColor = negroBench,
    )
}

@Composable
fun ExerciseListOrSelected(
    viewModel: CrearRutinaViewModel,
    filteredExercises: List<String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(negroBench)
    ) {
        if (viewModel.nombreEjercicioSeleccionado.isNotBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(rojoBench.copy(alpha = 0.2f))
                    .padding(16.dp)
            ) {
                Text(
                    text = viewModel.nombreEjercicioSeleccionado,
                    color = rojoBench,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    viewModel.nombreEjercicioSeleccionado = ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Deseleccionar",
                        tint = rojoBench
                    )
                }
            }
        } else {
            if (filteredExercises.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 300.dp, min = 300.dp)
                        .padding(8.dp)
                ) {
                    items(filteredExercises) { ejercicio ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (ejercicio == viewModel.nombreEjercicioSeleccionado)
                                        rojoBench.copy(alpha = 0.2f)
                                    else negroOscuroBench
                                )
                                .clickable {
                                    viewModel.nombreEjercicioSeleccionado = ejercicio
                                }
                        ) {
                            Text(
                                text = ejercicio,
                                color = rojoBench,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(negroBench)
                ) {
                    Text(
                        text = "No se encuentran ejercicios",
                        color = rojoBench,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SeriesRepsFields(viewModel: CrearRutinaViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(0.4f)) {
            Text(
                text = "Series",
                color = rojoBench,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            GlobalTextField(
                nombre = "Series",
                text = viewModel.series.toString(),
                colorText = rojoBench,
                onValueChange = { viewModel.series = it.toIntOrNull() ?: 0 },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = negroBench,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Spacer(Modifier.width(20.dp))
        Column(Modifier.weight(0.4f)) {
            Text(
                text = "Repeticiones",
                color = rojoBench,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            GlobalTextField(
                nombre = "Repeticiones",
                text = viewModel.repeticiones.toString(),
                colorText = rojoBench,
                onValueChange = { viewModel.repeticiones = it.toIntOrNull() ?: 0 },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = negroBench,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }
}

@Composable
fun ErrorMessage(errorMessage: MutableState<String>) {
    if (errorMessage.value.isNotEmpty()) {
        Text(
            text = errorMessage.value,
            color = Color.Red,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Composable
fun ActionButtons(
    onDismiss: () -> Unit,
    onAdd: (ExerciseRoutineEntry) -> Unit,
    viewModel: CrearRutinaViewModel,
    errorMessage: MutableState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlobalButton(
            text = "Cancelar",
            colorText = rojoBench,
            backgroundColor = negroBench,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            onClick = { onDismiss() }
        )

        GlobalButton(
            text = "Añadir",
            colorText = rojoBench,
            backgroundColor = negroBench,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            onClick = {
                val seriesValid = viewModel.series
                val repeticionesValid = viewModel.repeticiones

                when {
                    viewModel.nombreEjercicioSeleccionado.isBlank() -> {
                        errorMessage.value = "Selecciona un ejercicio."
                    }

                    seriesValid <= 0 -> {
                        errorMessage.value = "Introduce un número válido de series."
                    }

                    repeticionesValid <= 0 -> {
                        errorMessage.value = "Introduce un número válido de repeticiones."
                    }

                    else -> {
                        errorMessage.value = ""
                        val ejercicio = ExerciseRoutineEntry(
                            nombre = viewModel.nombreEjercicioSeleccionado,
                            series = seriesValid,
                            repeticiones = repeticionesValid
                        )
                        onAdd(ejercicio)
                        viewModel.resetFormularioEjercicio()
                    }
                }
            }
        )
    }
}

@Composable
fun BotonCrearRutina(viewModel: CrearRutinaViewModel) {
    GlobalButton(
        colorText = rojoBench,
        text = "Crear rutina", modifier = Modifier,
        backgroundColor = negroOscuroBench
    ) {
        viewModel.guardarRutinaEnFirebase(
            onSuccess = { id ->
                // Aquí manejas el éxito (id es el String con el ID de la rutina)
                println("Rutina guardada con ID: $id")
                // Puedes navegar a otra pantalla o mostrar un mensaje
            },
            onFailure = { exception ->
                // Aquí manejas el error
                println("Error al guardar: ${exception.message}")
                // Puedes mostrar un mensaje de error al usuario
            }
        )    }
}