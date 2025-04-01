package com.oscar.benchfitness.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.oscar.benchfitness.components.GlobalBarraNavegacion
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.home.PrincipalViewModel

@Composable
fun PrincipalScreen(navController: NavController, viewModel: PrincipalViewModel) {

    val nombre by viewModel.nombre.collectAsState()
    val objetivo by viewModel.objetivo.collectAsState()
    val calorias by viewModel.calorias.collectAsState()

    Scaffold(bottomBar = {
        Box(
            modifier = Modifier
                .padding(vertical = 15.dp)
        ) {
            GlobalBarraNavegacion()
        }
    }) { paddingValues ->
        PrincipalBodyContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel,
            objetivo = objetivo,
            nombre = nombre,
            calorias = calorias
        )
    }

}

@Composable
fun PrincipalBodyContent(
    navController: NavController,
    modifier: Modifier,
    viewModel: PrincipalViewModel,
    nombre: String,
    objetivo: String,
    calorias: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
    ) {
        GlobalHeader("Bienvenido $nombre")
        Spacer(modifier = Modifier.height(15.dp))
        ObjetivoUsuario(objetivo)
        Spacer(modifier = Modifier.height(15.dp))
        RecomendacionObjetivo(
            viewModel.interpretarObjetivo(objetivo = objetivo),
            calorias
        )
        Spacer(modifier = Modifier.height(15.dp))
        BloqueApuntarRutina()
        Spacer(modifier = Modifier.height(15.dp))
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
fun RecomendacionObjetivo(nombreObjetivo: String, calorias: String) {
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
                    Icon(Icons.Filled.Info, contentDescription = "Info", tint = rojoBench)
                }
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
                .background(negroOscuroBench),
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
fun BloqueApuntarRutina() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(325.dp)
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                negroOscuroBench
            )
    ) {
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Rutina de hoy", style = MaterialTheme.typography.bodySmall,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.7f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        negroBench
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Sin asignar", style = MaterialTheme.typography.bodySmall,
                    fontSize = 18.sp,
                    color = rojoBench
                )
            }

        }
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Apunta tu entrenamiento de hoy", style = MaterialTheme.typography.bodyMedium,
                fontSize = 17.sp,
                color = Color.White
            )
            Spacer(Modifier.height(10.dp))
            Image(
                painter = painterResource(id = R.drawable.anadir),
                contentDescription = "añadir",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}
