package com.oscar.benchfitness.screens.exercises

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.CrearRutina
import com.oscar.benchfitness.navegation.Rutina
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.exercises.RutinasViewModel

@Composable
fun RutinasScreen(navController: NavController, viewModel: RutinasViewModel) {

    // Obtener rutinas al cargar la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerRutinas()
    }

    // Usamos verticalScroll para hacer que la columna sea desplazable
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
            .clickable { navController.navigate(CrearRutina) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("Crear rutina", color = rojoBench, fontWeight = FontWeight.Normal)
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
                        navController.navigate(Rutina)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(rutina.nombre, color = Color.White, fontWeight = FontWeight.Normal)
                Text(
                    rutina.objetivo, color = rojoBench, fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
