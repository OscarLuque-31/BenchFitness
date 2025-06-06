package com.oscar.benchfitness.screens.workout.routines

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.oscar.benchfitness.models.routines.ExerciseRoutineEntry
import com.oscar.benchfitness.navegation.Rutinas
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.utils.interpretarDia
import com.oscar.benchfitness.viewModels.workout.CrearRutinaViewModel

@Composable
fun CrearRutinaScreen(navController: NavController, viewModel: CrearRutinaViewModel) {

    // Obtiene el nombre de todos los ejercicios
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
        BoxAnadirEjerciciosPorDia(viewModel, navController)
        Spacer(Modifier.height(20.dp))

        // Abre el dialogo para agregar ejercicios
        if (viewModel.showDialog) {
            DialogAddExercise(
                onDismiss = { viewModel.showDialog = false },
                onAdd = { ejercicioRoutineEntry ->
                    // Agrega el ejercicio al dia asignado
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
                viewModel = viewModel
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
            onValueChange = {
                viewModel.objetivo = it
                // Da una recomendación al usuario según su objetivo
                viewModel.obtenerRecomendacionesPorObjetivo()
            },
            opciones = objetivos,
            modifier = Modifier
                .widthIn(min = 150.dp)
                .height(50.dp),
            backgroundColor = negroBench,
            colorText = rojoBench
        )
    }
    MostrarRecomendaciones(viewModel)
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

                // Si se ha pulsado el dia se añade a los dias seleccinados
                if (pulsado.value) {
                    viewModel.diasSeleccionados += diaCompleto
                    // Si el dia seleccionado esta vacio se asigna el dia completo
                    if (viewModel.diaSeleccionado.isBlank()) {
                        viewModel.diaSeleccionado = diaCompleto
                    }
                } else {
                    // Si no esta pulsado se quita de los dias seleccionados
                    viewModel.diasSeleccionados -= diaCompleto
                    // Si el dia Seleccionado es igual al dia completo se quita del dropdown
                    if (viewModel.diaSeleccionado == diaCompleto) {
                        // Si la lista no esta vacía se busca el primero que haya
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
    // Verifica si hay días seleccionados y si el objetivo esta seleccionado
    if (viewModel.diasSeleccionados.isNotEmpty() && viewModel.objetivo != "Objetivo") {
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
        // Si no hay días seleccionados, no muestra el Dropdown
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Selecciona al menos un día y un objetivo",
                color = rojoBench,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun BoxAnadirEjerciciosPorDia(viewModel: CrearRutinaViewModel, navController: NavController) {
    if (viewModel.diasSeleccionados.isNotEmpty() && viewModel.objetivo != "Objetivo") {
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
                // Si la lista de ejercicios esta vacía te saldrá que añadas ejercicios
                if (ejerciciosList.isEmpty()) {
                    Text(
                        "Añade ejercicios",
                        color = rojoBench,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                // Si seleccionas el ejercicio puedes borrarlo de la lista
                if (viewModel.ejercicioSeleccionado != null && ejerciciosList.isNotEmpty()) {
                    GlobalButton(
                        text = "Eliminar",
                        colorText = negroOscuroBench,
                        backgroundColor = rojoBench,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        // Elimina el ejercicio del dia elegido
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
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            BotonCrearRutina(viewModel, navController = navController)
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
        EncabezadoNombreSerieReps(negroOscuroBench)
        Spacer(Modifier.height(10.dp))

        LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
            items(ejerciciosList) { ejercicio ->

                val isSelected = viewModel.ejercicioSeleccionado == ejercicio
                val backgroundColor =
                    if (isSelected) rojoBench.copy(alpha = 0.2f) else negroOscuroBench

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            viewModel.ejercicioSeleccionado =
                                if (isSelected) null else ejercicio
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BoxExerciseEntry(
                        nombre = ejercicio.nombre,
                        series = ejercicio.series.toString(),
                        repeticiones = ejercicio.repeticiones.toString(),
                        backgroundColor = backgroundColor,
                        modifier = Modifier
                    )
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

    // Si el texto buscado no esta vacio el ejercicio seleccionado estará vacio
    LaunchedEffect(searchQuery.value) {
        if (searchQuery.value.isNotEmpty()) {
            viewModel.nombreEjercicioSeleccionado = ""
        }
    }

    // Filtra los ejercicios según la busqueda que hagamos
    val filteredExercises = remember(searchQuery.value, viewModel.listaEjercicios) {
        if (searchQuery.value.isEmpty()) viewModel.listaEjercicios
        else viewModel.listaEjercicios.filter { it.contains(searchQuery.value, ignoreCase = true) }
    }

    // Altura dinamica para mejor diseño
    val dynamicHeight =
        when (viewModel.nombreEjercicioSeleccionado.isNotEmpty() || filteredExercises.isEmpty()) {
            true -> 0.55f
            false -> 0.8f
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
                MostrarRecomendacionActual(viewModel)
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
        // Si el ejercicio seleccinado no esta vacio se puede deseleccionar el ejercicio
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
                    viewModel.deseleccionarEjercicio()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Deseleccionar",
                        tint = rojoBench
                    )
                }
            }
        } else {
            // Se filtran todos los ejercicios posibles a elegir
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
                                    // Se selecciona el ejercicio elegido
                                    viewModel.seleccionarEjercicio(ejercicio)
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
            GlobalTextField(
                nombre = "Series",
                text = viewModel.seriesText,
                colorText = rojoBench,
                onValueChange = { viewModel.seriesText = it },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = negroBench,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Spacer(Modifier.width(20.dp))
        Column(Modifier.weight(0.4f)) {
            GlobalTextField(
                nombre = "Reps",
                text = viewModel.repeticionesText,
                colorText = rojoBench,
                onValueChange = { viewModel.repeticionesText = it },
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
            colorText = negroOscuroBench,
            backgroundColor = rojoBench,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            onClick = {
                // Valida el ejercicio seleccionado
                val error = viewModel.validarEjercicio(viewModel.diaSeleccionado)

                if (error != null) {
                    errorMessage.value = error
                } else {
                    errorMessage.value = ""
                    // Crea la entrada del ejercicio
                    onAdd(viewModel.crearEntryEjercicio())
                    // Resetea el formulario
                    viewModel.resetFormularioEjercicio()
                }
            }
        )
    }
}

@Composable
fun BotonCrearRutina(viewModel: CrearRutinaViewModel, navController: NavController) {
    var mostrarDialogoError by remember { mutableStateOf(false) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    // Dialogo de error si ocurre algo
    if (mostrarDialogoError && viewModel.errorRutina != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoError = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoError = false }) {
                    Text(
                        "Aceptar", color = rojoBench, fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    )
                }
            },
            title = {
                Text(
                    text = "Error al crear rutina",
                    color = rojoBench, fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                )
            },
            text = {
                Text(
                    text = viewModel.errorRutina ?: "",
                    color = Color.White, fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                )
            },
            containerColor = negroBench,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Dialogo de éxito
    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoExito = false
                // Navega a rutinas al finalizar
                navController.navigate(Rutinas.route)
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoExito = false
                    navController.navigate(Rutinas.route)
                }) {
                    Text(
                        "Ver mis rutinas", color = rojoBench, fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    )
                }
            },
            title = {
                Text(
                    "¡Rutina creada!", color = rojoBench, fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                )
            },
            text = {
                Text(
                    "Tu rutina se ha guardado correctamente.",
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                )
            },
            containerColor = negroBench,
            shape = RoundedCornerShape(16.dp)
        )
    }

    GlobalButton(
        colorText = rojoBench,
        text = "Crear rutina",
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = negroBench
    ) {
        // Valida la rutina entera
        val esValido = viewModel.validarRutina()

        if (esValido) {
            // Guarda la rutina creada en las rutinas del usuario
            viewModel.guardarRutinaEnFirebase(
                onSuccess = { mostrarDialogoExito = true },
                onFailure = {
                    viewModel.errorRutina = "Error: ${it.message}"
                    mostrarDialogoError = true
                }
            )
        } else {
            mostrarDialogoError = true
        }
    }
}

@Composable
fun MostrarRecomendaciones(viewModel: CrearRutinaViewModel) {
    Column(modifier = Modifier.padding(vertical = 10.dp)) {
        if (viewModel.objetivo == "Hipertrofia" || viewModel.objetivo == "Fuerza") {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Recomendaciones para tu objetivo:",
                color = rojoBench,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.recomendaciones,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun EncabezadoNombreSerieReps(backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
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
}

@Composable
fun MostrarRecomendacionActual(viewModel: CrearRutinaViewModel) {
    val recomendacion = viewModel.obtenerRecomendacionActual()

    if (!recomendacion.isNullOrEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = recomendacion,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .background(negroBench, RoundedCornerShape(8.dp))
                .padding(12.dp)
        )
    }
}

@Composable
fun BoxExerciseEntry(
    nombre: String,
    series: String,
    repeticiones: String,
    backgroundColor: Color = negroBench,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(0.5f),
                text = nombre,
                color = rojoBench,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.weight(0.25f),
                text = series,
                color = verdePrincipiante,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.25f),
                text = repeticiones,
                color = azulIntermedio,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
