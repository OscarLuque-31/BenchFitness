package com.oscar.benchfitness.screens.workout.routines

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.CrearRutina
import com.oscar.benchfitness.navegation.Rutina
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.workout.RutinasViewModel

@Composable
fun RutinasScreen(navController: NavController, viewModel: RutinasViewModel) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Mostrar la lista de rutinas
        ListaRutinas(viewModel = viewModel, navController)

        // Mostrar la opción para crear una nueva rutina
        BoxNuevaRutina(navController)
    }
}

@Composable
fun BoxNuevaRutina(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
            .clickable { navController.navigate(CrearRutina.route) }
            .padding(horizontal = 50.dp), // Padding consistente
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Cambio clave
    ) {
        // Contenedor para el texto (misma estructura que las rutinas)
        Column(
            modifier = Modifier.weight(1f), // Ocupa el espacio disponible
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Crear rutina",
                color = rojoBench,
                fontWeight = FontWeight.Medium,
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Icono con tamaño fijo
        Image(
            painter = painterResource(id = R.drawable.anadir),
            contentDescription = "Añadir rutina",
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun ListaRutinas(viewModel: RutinasViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Iteramos sobre las rutinas
        viewModel.listaRutinas.forEach { rutina ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroOscuroBench)
                    .clickable {
                        navController.currentBackStackEntry?.savedStateHandle?.set("rutina", rutina)
                        navController.navigate(Rutina.route)
                    }
                    .padding(horizontal = 50.dp), // Padding consistente
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween // Cambio clave
            ) {
                // Contenedor para el nombre de la rutina
                Column(
                    modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = rutina.nombre,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))

                // Contenedor para el objetivo (tamaño fijo)
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = rutina.objetivo,
                        color = rojoBench,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

// VERSIÓN ALTERNATIVA: Si quieres un diseño más estructurado
@Composable
fun ListaRutinasAlternativa(viewModel: RutinasViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        viewModel.listaRutinas.forEach { rutina ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroOscuroBench)
                    .clickable {
                        navController.currentBackStackEntry?.savedStateHandle?.set("rutina", rutina)
                        navController.navigate(Rutina.route)
                    }
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Información de la rutina (ocupa 80% del ancho)
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Nombre de la rutina
                    Text(
                        text = rutina.nombre,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Objetivo de la rutina
                    Text(
                        text = rutina.objetivo,
                        color = rojoBench,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Espacio para icono o indicador (ocupa 20% del ancho)
                Column(
                    modifier = Modifier.weight(0.2f),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Puedes agregar un icono aquí si quieres
                    Text(
                        text = "→",
                        color = rojoBench,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}