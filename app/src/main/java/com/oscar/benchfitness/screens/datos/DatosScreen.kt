package com.oscar.benchfitness.screens.datos

import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.DatePickerField
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.navegation.Principal
import com.oscar.benchfitness.screens.ColumnaPrincipal
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.datos.DatosViewModel

@Composable
fun DatosScreen(navController: NavController, viewModel: DatosViewModel) {
    val context = LocalContext.current

    // Cargar los datos del usuario al mostrar la pantalla
    viewModel.comprobarBirthdayUsuario()

    DatosBodyContent(
        viewModel = viewModel,
        onStartClick = {
            viewModel.guardarDatosUsuario(
                context,
                onSuccess = {
                    navController.navigate(Principal)
                },
                onFailure = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                        .show()
                })
        }
    )

}

@Composable
fun DatosBodyContent(
    viewModel: DatosViewModel,
    onStartClick: () -> Unit
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

        ContenedorDatos(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f),
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
        Spacer(Modifier.height(25.dp))
        GlobalButton(
            "Empezar", negroBench,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colorText = Color.White,
        ) {
            onStartClick()
        }
    }
}

@Composable
fun FilaAlturayGenero(viewModel: DatosViewModel) {

    val opciones: List<String> = listOf("Hombre", "Mujer")

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
                .width(140.dp)
                .height(50.dp)
        )

        GlobalDropDownMenu(
            nombreSeleccion = viewModel.genero,
            opciones = opciones,
            onValueChange = { viewModel.genero = it },
            modifier = Modifier
                .width(140.dp)
                .height(50.dp)
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
        // GlobalTextField para el peso
        GlobalTextField(
            nombre = "Peso",
            text = viewModel.peso, // Valor del peso
            onValueChange = { viewModel.peso = it }, // Actualizar el estado del peso
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
                .width(140.dp)
                .height(50.dp)
        )

        // GlobalDropDownMenu para la experiencia
        GlobalDropDownMenu(
            nombreSeleccion = viewModel.experiencia,
            onValueChange = { viewModel.experiencia = it },
            opciones = opcionesExperiencia, // Lista de opciones de experiencia
            modifier = Modifier
                .width(160.dp)
                .height(50.dp)
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
        "Pérdida de peso",
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
                .height(50.dp)
        )
        GlobalDropDownMenu(
            viewModel.objetivo,
            opcionesObjetivoFit,
            onValueChange = { viewModel.objetivo = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Composable
fun FilaEdad(
    viewModel: DatosViewModel,
) {
    DatePickerField(onDateSelected = { viewModel.birthday = it })
}