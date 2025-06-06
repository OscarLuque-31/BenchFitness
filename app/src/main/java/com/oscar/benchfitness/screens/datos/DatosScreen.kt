package com.oscar.benchfitness.screens.datos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.animations.LoadingScreenCircularProgress
import com.oscar.benchfitness.components.DatePickerField
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Main
import com.oscar.benchfitness.screens.start.ColumnaPrincipal
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.calcularEdad
import com.oscar.benchfitness.viewModels.datos.DatosViewModel

@Composable
fun DatosScreen(navController: NavController, viewModel: DatosViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Muestra el snackbar si hay un error
    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    if (viewModel.isLoading) {
        LoadingScreenCircularProgress()
    } else {
        DatosBodyContent(
            viewModel = viewModel,
            onStartClick = {
                // Guarda los datos en base de datos
                viewModel.guardarDatosUsuario(
                    onSuccess = {
                        // Redirige al usuario a la pantalla principal
                        navController.navigate(Main.route) {
                            popUpTo(Inicio.route) { inclusive = true }
                        }
                    },
                    onFailure = { errorMessage ->
                        viewModel.snackbarMessage = errorMessage
                    })
            },
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun DatosBodyContent(
    viewModel: DatosViewModel,
    onStartClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
            .verticalScroll(rememberScrollState())
    ) {
        ColumnaPrincipal(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f),
            modifierImagen = Modifier.size(120.dp)
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            snackbar = { snackbarData ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = negroOscuroBench)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        color = rojoBench,
                        fontSize = 16.sp
                    )
                }
            }
        )

        ContenedorDatos(
            modifier = Modifier
                .fillMaxWidth(),
            viewModel,
            onStartClick
        )
    }
}

@Composable
fun ContenedorDatos(
    modifier: Modifier = Modifier,
    viewModel: DatosViewModel,
    onStartClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .fillMaxWidth()
            .background(color = rojoBench)
            .padding(24.dp)
    ) {
        Text(
            "Antes de empezar",
            fontSize = 24.sp,
            color = negroBench,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Por favor, introduce estos datos para ofrecerte estadísticas personalizadas y monitorear tu progreso de forma precisa",
            fontSize = 16.sp,
            color = negroBench,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMedium
        )
        FilaAlturayGenero(viewModel)
        ColumnaNivelYObjetivo(viewModel)
        FilaPesoYExperiencia(viewModel)
        FilaEdad(viewModel)
        Spacer(Modifier.height(12.5.dp))
        if (viewModel.birthday.isNotEmpty()) {
            val edadUsuario = calcularEdad(viewModel.birthday)

            if (edadUsuario.toInt() in 0..11) {
                Text(
                    "Advertencia: Esta app no está recomendada para menores de 12 años.",
                    color = Color.Yellow,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )
            }
        }
        GlobalButton(
            "Empezar", negroBench,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(top = 12.5.dp),
            colorText = Color.White,
        ) {
            onStartClick()
        }
    }
}

@Composable
fun FilaAlturayGenero(viewModel: DatosViewModel) {

    val opcionesSexo: List<String> = listOf("Hombre", "Mujer")

    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlobalTextField(
            nombre = "Altura",
            text = viewModel.altura,
            onValueChange = { viewModel.altura = it },
            trailingIcon = {
                Text(
                    "cm",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 14.sp,
                    color = negroBench
                )
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(min = 100.dp, max = 140.dp)
                .height(50.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(Modifier.width(30.dp))
        GlobalDropDownMenu(
            nombreSeleccion = viewModel.genero,
            opciones = opcionesSexo,
            onValueChange = { viewModel.genero = it },
            modifier = Modifier
                .weight(0.3f)
                .height(50.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench,
            colorItemPulsado = rojoBench.copy(alpha = 0.7f)
        )
    }
}


@Composable
fun FilaPesoYExperiencia(viewModel: DatosViewModel) {

    val opcionesExperiencia: List<String> = listOf("Principiante", "Intermedio", "Avanzado")

    Row(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GlobalTextField(
            nombre = "Peso",
            text = viewModel.peso,
            onValueChange = { viewModel.peso = it },
            trailingIcon = {
                Text(
                    "kg",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 14.sp,
                    color = negroBench
                )
            },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(min = 100.dp, max = 140.dp)
                .height(50.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(Modifier.width(30.dp))
        GlobalDropDownMenu(
            nombreSeleccion = viewModel.experiencia,
            onValueChange = { viewModel.experiencia = it },
            opciones = opcionesExperiencia,
            modifier = Modifier
                .height(50.dp),
            backgroundColor = Color.White, colorText = negroOscuroBench,
            colorItemPulsado = rojoBench.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ColumnaNivelYObjetivo(viewModel: DatosViewModel) {

    val opcionesNivelActividad: List<String> = listOf(
        "Sedentario (poco o ningún ejercicio)",
        "Ligera actividad (1-3 días/semana)",
        "Actividad moderada (3-5 días/semana)",
        "Alta actividad (6-7 días/semana)",
        "Actividad muy intensa (entrenamientos extremos)"
    )
    val opcionesObjetivoFit: List<String> = listOf(
        "Perder peso",
        "Mantener peso",
        "Masa muscular"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalDropDownMenu(
            viewModel.nivelActividad,
            opcionesNivelActividad,
            onValueChange = { viewModel.nivelActividad = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            backgroundColor = Color.White, colorText = negroOscuroBench,
            colorItemPulsado = rojoBench.copy(alpha = 0.7f)
        )
        GlobalDropDownMenu(
            viewModel.objetivo,
            opcionesObjetivoFit,
            onValueChange = { viewModel.objetivo = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            backgroundColor = Color.White, colorText = negroOscuroBench,
            colorItemPulsado = rojoBench.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun FilaEdad(
    viewModel: DatosViewModel,
) {
    DatePickerField(onDateSelected = { viewModel.birthday = it })
}