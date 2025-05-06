package com.oscar.benchfitness.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.navegation.Goal
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.interpretarObjetivo
import com.oscar.benchfitness.viewModels.home.HomeViewModel


@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel, user: userData) {

    val calorias by viewModel.calorias.collectAsState()

    HomeBodyContent(
        navController = navController,
        userData = user,
        calorias = calorias,
    )

}

@Composable
fun HomeBodyContent(
    navController: NavController,
    userData: userData,
    calorias: String,
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
                calorias,
                navController,
                userData
            )
            Spacer(modifier = Modifier.height(15.dp))
            BloqueApuntarRutina(
                modifier = Modifier.weight(1f)
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
    userData: userData
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
fun BloqueApuntarRutina(modifier: Modifier = Modifier) {
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
                modifier = Modifier.fillMaxWidth().weight(0.1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Rutina de hoy",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Contenedor de "Sin asignar"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroBench)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Sin asignar",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 18.sp,
                    color = rojoBench
                )
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
