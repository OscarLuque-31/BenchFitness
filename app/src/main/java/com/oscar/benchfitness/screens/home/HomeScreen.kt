package com.oscar.benchfitness.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.animations.LoadingScreen
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.models.Routine
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.navegation.Goal
import com.oscar.benchfitness.screens.exercises.BoxExerciseEntry
import com.oscar.benchfitness.screens.exercises.EncabezadoNombreSerieReps
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.interpretarObjetivo
import com.oscar.benchfitness.viewModels.home.HomeViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel, user: userData) {

    HomeBodyContent(
        navController = navController,
        userData = user,
        viewModel = viewModel
    )

}

@Composable
fun HomeBodyContent(
    navController: NavController,
    userData: userData,
    viewModel: HomeViewModel
) {
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
            ObjetivoUsuario(userData.objetivo)
            Spacer(modifier = Modifier.height(15.dp))
            RecomendacionObjetivo(
                interpretarObjetivo(objetivo = userData.objetivo),
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
                    title = "Objetivo",
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

    LaunchedEffect(Unit) {
        viewModel.comprobarRutinaAsignada()
    }


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
                Text(
                    "Rutina de hoy",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 20.sp,
                    color = Color.White
                )
                if (viewModel.isRutinaAsignada) {
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

            // Dialog cambiar rutina
            if (viewModel.cambiarRutina) {
                DialogoCambiarRutina(viewModel)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Contenedor de "Sin asignar"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
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
                                .verticalScroll(rememberScrollState())
                        ) {
                            DatosRutinaXDia(viewModel.userData.rutinaAsignada)
                        }

                    } else {
                        Column(
                            modifier = Modifier.padding(10.dp).fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
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

            // Sección de "Apunta tu entrenamiento"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Apunta tu entrenamiento de hoy",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 17.sp,
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.anadir),
                    contentDescription = "añadir",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

@Composable
fun DatosRutinaXDia(rutina: Routine) {

    // Dia actual
    val hoy = LocalDate.now()
    val diaActual = hoy.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        .replaceFirstChar { it.uppercase() }

    val entrenamientoHoy = rutina.dias.firstOrNull { it.dia == diaActual }


    entrenamientoHoy?.let { dia ->
        // Si existe rutina para hoy, mostrarla
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(negroBench)
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
        // Si no hay rutina hoy, puedes dejar esto vacío o mostrar un mensaje opcional
        Log.d("Rutina", "No hay rutina asignada para hoy ($diaActual)")
    }

}

@Composable
fun InfoObjetivo(objetivo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Estas son las calorías aproximadas que debes consumir si tu objetivo es $objetivo",
                color = rojoBench
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Recuerda,estas son unas cifras aproximadas,para más información contacte con un profesional",
                textAlign = TextAlign.Justify
            )
        }

    }
}

@Composable
fun DialogoCambiarRutina(viewModel: HomeViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.cambiarRutina = !viewModel.cambiarRutina },
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
