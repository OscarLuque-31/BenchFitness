package com.oscar.benchfitness.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.data.FirebaseData
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.DatosViewModel
import com.oscar.benchfitness.widgets.GlobalButton
import com.oscar.benchfitness.widgets.GlobalDropDownMenu
import com.oscar.benchfitness.widgets.GlobalTextField

@Composable
fun DatosScreen(navController: NavController, db: FirebaseFirestore, auth: FirebaseAuth) {

    val viewModel: DatosViewModel = viewModel();

    Scaffold { paddingValues ->
        DatosBodyContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            db = db,
            auth = auth,
            viewModel = viewModel
        )
    }
}

@Composable
fun DatosBodyContent(
    navController: NavController,
    modifier: Modifier,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    viewModel: DatosViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
    ) {
        // Columna principal centrada
        ColumnaPrincipal(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f) // Ocupa el espacio disponible
        )

        // Contenedor de bienvenida en la parte inferior
        ContenedorDatos(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f),
            navController,
            auth,
            db,
            viewModel
        )
    }
}

@Composable
fun ContenedorDatos(
    modifier: Modifier = Modifier,
    navController: NavController,
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    viewModel: DatosViewModel
) {

    val context = LocalContext.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .fillMaxWidth()
            .background(color = rojoBench)
            .padding(24.dp) // Espaciado interno
    ) {
        Text(
            "Antes de empezar",
            fontSize = 24.sp,
            color = negroBench,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre textos
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
        GlobalButton(
            "Empezar", negroBench, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), colorText = Color.White
        ) {
            val (isValid, errorMessage) = validateFieldsDatos(
                altura = viewModel.altura,
                genero = viewModel.genero,
                nivelActividad = viewModel.nivelActividad,
                objetivo = viewModel.objetivo,
                peso = viewModel.peso,
                experiencia = viewModel.experiencia
            )

            if (!isValid) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                FirebaseData().guardarDatosUsuario(
                    auth = auth,
                    db = db,
                    navController = navController,
                    datos = mapOf(
                        "altura" to viewModel.altura,
                        "genero" to viewModel.genero,
                        "peso" to viewModel.peso,
                        "experiencia" to viewModel.experiencia,
                        "nivelActividad" to viewModel.nivelActividad,
                        "objetivo" to viewModel.objetivo
                    ))
            }
        }
    }
}

@Composable
fun FilaAlturayGenero(viewModel: DatosViewModel) {

    val opciones: List<String> = listOf("Hombre", "Mujer")

    Row(
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // GlobalTextField para la altura
        GlobalTextField(
            nombre = "Altura",
            text = viewModel.altura, // Pasar el valor del estado
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

        // GlobalDropDownMenu para el género
        GlobalDropDownMenu(
            nombreSeleccion = viewModel.genero,
            opciones = opciones,
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
            .height(130.dp)
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
            nombreSeleccion = viewModel.experiencia, // Opción seleccionada de experiencia
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
        "Actividad muy intensa (entrenamientos físicos extremos)"
    )
    val opcionesObjetivoFit: List<String> = listOf(
        "Pérdida de peso",
        "Mantener peso",
        "Ganar masa muscular"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalDropDownMenu(
            viewModel.nivelActividad,
            opcionesNivelActividad,
            Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        GlobalDropDownMenu(
            viewModel.objetivo,
            opcionesObjetivoFit,
            Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

fun validateFieldsDatos(
    altura: String, genero: String, nivelActividad: String, objetivo: String,
    peso: String, experiencia: String
): Pair<Boolean, String> {
    return when {
        altura.isBlank() -> Pair(false, "La altura es obligatoria")
        genero.isBlank() -> Pair(false, "El género es obligatorio")
        nivelActividad.isBlank() -> Pair(false, "Debes seleccionar un nivel de actividad")
        objetivo.isBlank() -> Pair(false, "Debes seleccionar un objetivo fitness")
        peso.isBlank() -> Pair(false, "El peso es obligatorio")
        experiencia.isBlank() -> Pair(false, "Debes seleccionar tu nivel de experiencia")
        else -> Pair(true, "")
    }
}
