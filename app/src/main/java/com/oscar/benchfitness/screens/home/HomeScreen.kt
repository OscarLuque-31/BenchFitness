package com.oscar.benchfitness.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.animations.LoadingScreen
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.navegation.Goal
import com.oscar.benchfitness.screens.workout.routines.BoxExerciseEntry
import com.oscar.benchfitness.screens.workout.routines.EncabezadoNombreSerieReps
import com.oscar.benchfitness.ui.theme.amarilloAvanzado
import com.oscar.benchfitness.ui.theme.azulIntermedio
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.utils.interpretarObjetivo
import com.oscar.benchfitness.viewModels.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    HomeBodyContent(
        navController = navController,
        viewModel = viewModel
    )
}

@Composable
fun HomeBodyContent(
    navController: NavController,
    viewModel: HomeViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.comprobarRutinaAsignada()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Spacer(Modifier.height(20.dp))
            ObjetivoUsuario(viewModel.userData.objetivo)
            Spacer(modifier = Modifier.height(15.dp))
            RecomendacionObjetivo(
                interpretarObjetivo(objetivo = viewModel.userData.objetivo),
                viewModel.calorias,
                navController,
            )
            Spacer(modifier = Modifier.height(15.dp))
            BloqueApuntarRutina(
                modifier = Modifier.weight(1f), viewModel = viewModel
            )
        }
    }
}

@Composable
fun ObjetivoUsuario(objetivo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Objetivo", color = Color.White, fontWeight = FontWeight.Normal, fontSize = 20.sp
        )

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(negroBench)
                .padding(20.dp)
        ) {
            Text(
                text = objetivo,
                color = rojoBench,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        }
    }
}

@Composable
fun RecomendacionObjetivo(
    nombreObjetivo: String,
    calorias: String,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(negroOscuroBench),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            var showInfoDialog by remember { mutableStateOf(false) }

            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        nombreObjetivo, color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(
                        onClick = {
                            showInfoDialog = !showInfoDialog
                        }, modifier = Modifier.size(20.dp) // Tamaño más compacto
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Info", tint = rojoBench)
                    }
                }
                InfoDialog(
                    title = "Información",
                    showDialog = showInfoDialog,
                    onDismiss = { showInfoDialog = false },
                    cuerpo = { InfoObjetivo(nombreObjetivo) })
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    "$calorias kcal", color = rojoBench,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Column {
                Image(
                    painter = painterResource(id = R.drawable.cuerpo_definicion),
                    contentDescription = "cuerpo",
                    modifier = Modifier.size(70.dp)
                )
            }
        }
        Spacer(Modifier.width(20.dp))
        Row(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(negroOscuroBench)
                .clickable {
                    navController.navigate(Goal.route) {
                        launchSingleTop = true
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "arrowForward",
                tint = rojoBench
            )
        }
    }
}


@Composable
fun BloqueApuntarRutina(modifier: Modifier = Modifier, viewModel: HomeViewModel) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Título "Rutina de hoy"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Rutina",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(Modifier.width(10.dp))
                    if (viewModel.isRutinaAsignada) {
                        Text(
                            viewModel.userData.rutinaAsignada.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp,
                            color = rojoBench,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(0.6f) // Ocupa el 60% del ancho disponible
                        )
                    }
                }
                if (viewModel.isRutinaAsignada) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (viewModel.entrenamientoDelDia != null) {
                            IconButton(
                                onClick = {
                                    viewModel.apuntarRutina = !viewModel.apuntarRutina
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.anadir),
                                    contentDescription = "Apuntar rutina",
                                    tint = rojoBench,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(20.dp))
                        IconButton(
                            onClick = {
                                viewModel.cambiarRutina = !viewModel.cambiarRutina
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar rutina",
                                tint = rojoBench,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            }

            // Dialog cambiar rutina
            if (viewModel.cambiarRutina) {
                DialogoCambiarRutina(viewModel)
            }

            // Dialog cambiar rutina
            if (viewModel.apuntarRutina) {
                DialogoApuntarRutina(viewModel)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Contenedor de "Sin asignar"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroBench)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.rutinas.isEmpty()) {

                    Text(
                        "No hay rutinas que asignar",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 18.sp,
                        color = rojoBench
                    )
                } else if (viewModel.isRoutineLoading) {
                    LoadingScreen()
                } else {
                    if (viewModel.isRutinaAsignada) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            DatosRutinaXDia(viewModel)
                        }

                    } else {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                "Seleccione la rutina a asignar",
                                fontSize = 18.sp,
                                color = rojoBench,
                                fontWeight = FontWeight.Medium
                            )
                            GlobalDropDownMenu(
                                selectedItem = viewModel.rutinaSeleccionada,
                                backgroundColor = negroOscuroBench,
                                colorText = rojoBench,
                                colorFlechita = rojoBench,
                                opciones = viewModel.rutinas,
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = { rutinaSeleccionada ->
                                    viewModel.rutinaSeleccionada = rutinaSeleccionada
                                },
                                itemText = { it.nombre })
                            GlobalButton(
                                colorText = negroOscuroBench,
                                text = "Asignar",
                                backgroundColor = rojoBench,
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                tamanyoLetra = 16.sp
                            ) {
                                viewModel.asignarRutina()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DatosRutinaXDia(viewModel: HomeViewModel) {

    LaunchedEffect(viewModel.userData.rutinaAsignada) {
        viewModel.cargarDiaEntrenamiento()
    }

    viewModel.entrenamientoDelDia?.let { dia ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(negroBench)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = dia.dia,
                color = rojoBench,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(10.dp))
            EncabezadoNombreSerieReps(negroOscuroBench)

            dia.ejercicios.forEach { ejercicio ->
                BoxExerciseEntry(
                    nombre = ejercicio.nombre,
                    series = ejercicio.series.toString(),
                    repeticiones = ejercicio.repeticiones.toString(),
                    backgroundColor = negroOscuroBench,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(negroBench),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No hay entrenamiento hoy",
                color = rojoBench,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun InfoObjetivo(objetivo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estas son las calorías aproximadas que debes consumir si tu objetivo es $objetivo.",
            color = rojoBench,
            fontSize = 20.sp,
            textAlign = TextAlign.Justify
        )

        Text(
            text = "Recuerda: estas cifras son aproximadas. Para un plan adecuado, consulta con un profesional.",
            color = Color.Gray,
            fontSize = 15.sp,
            textAlign = TextAlign.Justify,
        )
    }
}


@Composable
fun DialogoCambiarRutina(viewModel: HomeViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.cambiarRutina = !viewModel.cambiarRutina },
        confirmButton = {
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
                    onClick = { viewModel.cambiarRutina = !viewModel.cambiarRutina }
                )
                GlobalButton(
                    text = "Cambiar",
                    backgroundColor = rojoBench,
                    colorText = negroOscuroBench,
                    onClick = {
                        viewModel.desasignarRutina()
                        viewModel.cambiarRutina = false
                    },
                    modifier = Modifier
                )
            }
        },
        title = {
            Text(
                text = "Cambiar rutina",
                color = rojoBench,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "¿Estás seguro de que quieres cambiar esta rutina? Luego podrás asignarla de nuevo si quieres.",
                color = Color.White,
                fontSize = 16.sp
            )
        },
        containerColor = negroOscuroBench,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun DialogoApuntarRutina(viewModel: HomeViewModel) {
    val dia = viewModel.entrenamientoDelDia
    val progresoDiario = viewModel.dailyExerciseProgress
    val expandedItems = remember { mutableStateMapOf<String, Boolean>() }

    val showError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    // Auto cerrar el snackbar después de 3 segundos
    LaunchedEffect(showError.value) {
        if (showError.value) {
            delay(4000)
            showError.value = false
        }
    }

    LaunchedEffect(dia) {
        dia?.let { viewModel.initDailyExerciseProgress(it) }
    }

    if (dia != null && progresoDiario.isNotEmpty()) {
        Dialog(onDismissRequest = { viewModel.apuntarRutina = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(negroBench)
                    .padding(20.dp)
            ) {
                Text(
                    text = dia.dia.uppercase(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = rojoBench,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn {
                    itemsIndexed(dia.ejercicios) { ejercicioIndex, ejercicio ->
                        val progresoEjercicio = progresoDiario[ejercicioIndex]
                        val ejecucionHoy =
                            progresoEjercicio.historial.first { it.fecha == viewModel.currentDate }
                        val completado = ejecucionHoy.completado
                        val isExpanded = expandedItems[ejercicio.nombre] ?: false

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(negroOscuroBench)
                                .clickable {
                                    expandedItems[ejercicio.nombre] = !isExpanded
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = ejercicio.nombre,
                                    color = rojoBench,
                                    fontSize = 16.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.width(10.dp))
                                if (completado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Completado",
                                        tint = verdePrincipiante,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            if (isExpanded) {
                                Spacer(Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            "Series",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                    }
                                }

                                ejecucionHoy.series.forEachIndexed { setIndex, set ->
                                    var repsInput by remember(progresoEjercicio) {
                                        mutableStateOf(if (set.reps > 0) set.reps.toString() else "")
                                    }
                                    var pesoInput by remember(progresoEjercicio) {
                                        mutableStateOf(if (set.peso > 0.0) set.peso.toString() else "")
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${setIndex + 1}",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 20.dp)
                                        )

                                        Box(
                                            modifier = Modifier.weight(1.5f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GlobalTextField(
                                                nombre = "Reps",
                                                text = repsInput,
                                                onValueChange = { input ->
                                                    repsInput = input
                                                    viewModel.onRepsInput(
                                                        ejercicioNombre = ejercicio.nombre,
                                                        setIndex = setIndex,
                                                        input = input
                                                    )
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                backgroundColor = negroBench,
                                                colorText = azulIntermedio,
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                            )
                                        }
                                        Spacer(Modifier.width(20.dp))
                                        Box(
                                            modifier = Modifier.weight(1.5f),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            GlobalTextField(
                                                nombre = "Peso",
                                                text = pesoInput,
                                                onValueChange = { input ->
                                                    pesoInput = input
                                                    viewModel.onPesoInput(
                                                        ejercicioNombre = ejercicio.nombre,
                                                        setIndex = setIndex,
                                                        input = input
                                                    )
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                backgroundColor = negroBench,
                                                colorText = amarilloAvanzado,
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                            )
                                        }
                                    }
                                }

                                GlobalButton(
                                    text = "Completar",
                                    backgroundColor = verdePrincipiante,
                                    colorText = negroBench,
                                    tamanyoLetra = 15.sp,
                                    onClick = {
                                        val allSetsValid = ejecucionHoy.series.all { set ->
                                            val repsValid =
                                                viewModel.isValidReps(set.reps.toString())
                                            val pesoValid =
                                                viewModel.isValidPeso(set.peso.toString())
                                            repsValid && pesoValid
                                        }

                                        if (allSetsValid) {
                                            viewModel.marcarEjercicioCompletado(ejercicio.nombre)
                                            expandedItems[ejercicio.nombre] = false
                                        } else {
                                            errorMessage.value =
                                                "Completa correctamente todas las series (reps > 0 y peso ≥ 0)"
                                            showError.value = true
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                )

                                if (showError.value) {
                                    ErrorSnackbar(
                                        message = errorMessage.value,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                GlobalButton(
                    text = "Cerrar",
                    backgroundColor = rojoBench,
                    colorText = Color.White,
                    onClick = { viewModel.apuntarRutina = false },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ErrorSnackbar(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(negroBench)
            .border(1.dp, rojoBench, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = rojoBench,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = message,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

