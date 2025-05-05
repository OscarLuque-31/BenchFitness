package com.oscar.benchfitness.screens.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.navegation.Deficit
import com.oscar.benchfitness.navegation.Metabolismo
import com.oscar.benchfitness.navegation.Peso
import com.oscar.benchfitness.navegation.Progreso
import com.oscar.benchfitness.navegation.RepeMaxima
import com.oscar.benchfitness.navegation.Superavit
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench


@Composable
fun StatisticsScreen(navController: NavController) {
    Column {
        GlobalHeader("Estadísticas")
        ColumnaOpciones(navController)
    }
}

@Composable
fun ColumnaOpciones(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3.5f)  // Asigna peso a la fila superior
        ) {
            ColumnaPesoMantenerSuperavit(
                navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            ColumnaProgresoDeficit(
                navController,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))  // Espacio entre secciones

        CajaRepeMaxima(
            navController,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)  // Peso para la caja inferior
        )
    }
}

@Composable
fun ColumnaProgresoDeficit(navController: NavController, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CajaProgreso(navController, modifier = Modifier.weight(1f))
        Spacer(Modifier.padding(8.dp))
        CajaDeficit(navController, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ColumnaPesoMantenerSuperavit(navController: NavController, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        CajaPeso(navController, modifier = Modifier.weight(1f))
        Spacer(Modifier.padding(8.dp))
        RowTituloCalculos()
        Spacer(Modifier.padding(8.dp))
        CajaMantener(navController, modifier = Modifier.weight(1f))
        Spacer(Modifier.padding(8.dp))
        CajaSuperavit(navController, modifier = Modifier.weight(1f))
    }
}

@Composable
fun RowTituloCalculos() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(rojoBench),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Cálculos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = negroOscuroBench)
    }
}


@Composable
fun CajaProgreso(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.foto_progreso),
            contentDescription = "imagen progreso",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable {
                    navController.navigate(Progreso.route)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Progreso", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = rojoBench)
        }

    }
}

@Composable
fun CajaPeso(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.foto_peso),
            contentDescription = "imagen peso",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable {
                    navController.navigate(Peso.route)
                },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Peso", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = rojoBench)
        }
    }
}

@Composable
fun CajaMantener(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
            .clickable {
                navController.navigate(Metabolismo.route)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mantener", fontSize = 20.sp, fontWeight = FontWeight.Normal, color = rojoBench)
            Spacer(Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.metabolismo),
                contentDescription = "imagen metabolismo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CajaSuperavit(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
            .clickable {
                navController.navigate(Superavit.route)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Superávit", fontSize = 20.sp, fontWeight = FontWeight.Normal, color = rojoBench)
            Spacer(Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.superavit),
                contentDescription = "imagen superavit",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CajaDeficit(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
            .clickable {
                navController.navigate(Deficit.route)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Déficit", fontSize = 20.sp, fontWeight = FontWeight.Normal, color = rojoBench)
            Spacer(Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.cuerpo_definicion),
                contentDescription = "imagen definicion",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CajaRepeMaxima(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
            .clickable {
                navController.navigate(RepeMaxima.route)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Repetición máxima",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = rojoBench
            )
            Spacer(Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.repe_maxima),
                contentDescription = "imagen RM",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}