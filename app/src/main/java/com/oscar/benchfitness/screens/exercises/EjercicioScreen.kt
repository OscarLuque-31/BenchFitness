package com.oscar.benchfitness.screens.exercises


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.animations.AnimatedFavoriteStar
import com.oscar.benchfitness.components.AdaptiveGifRow
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.models.exercises.ExerciseData
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.exercises.EjercicioViewModel

@Composable
fun EjercicioScreen(
    navController: NavController,
    viewModel: EjercicioViewModel,
    ejercicio: ExerciseData,
    urlGIF: String
) {

    LaunchedEffect(viewModel) {
        viewModel.checkIfFavoriteUI(ejercicio.id_ejercicio)
    }

    Column(
        modifier = Modifier
            .padding(top = 15.dp, start = 20.dp, end = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        EjercicioBodyContent(navController, ejercicio, viewModel, urlGIF)
    }
}

@Composable
fun EjercicioBodyContent(
    navController: NavController,
    ejercicio: ExerciseData,
    viewModel: EjercicioViewModel,
    urlGIF: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(negroOscuroBench)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.flechita_atras),
                    contentDescription = "Flechita atrás",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { navController.popBackStack() }
                )
                IconToggleButton(
                    checked = viewModel.isFavorite,
                    onCheckedChange = {
                        viewModel.toogleFavoriteUI(exerciseData = ejercicio)
                    }
                ) {
                    AnimatedFavoriteStar(isFavorite = viewModel.isFavorite)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(ejercicio.nombre, color = rojoBench)
            Spacer(Modifier.height(10.dp))
            Text(ejercicio.descripcion, color = Color.White, fontSize = 15.sp)
            Spacer(Modifier.height(30.dp))
            DatosEspecificosEjercicio(ejercicio, urlGIF)
        }
    }
}

@Composable
fun DatosEspecificosEjercicio(ejercicio: ExerciseData, urlGIF: String) {

    // Columna principal
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        FilaPrincipal(ejercicio)
        FilaEquipamiento(ejercicio.equipamiento)
        FilaGif(urlGIF)
        FilaInstrucciones(ejercicio.instrucciones)

    }
}


@Composable
fun FilaPrincipal(ejercicio: ExerciseData) {
    // Fila de categoria/tipo_fuerza y musculo principal y secundario
    Row(
        modifier = Modifier.height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Columna de categoria / fuerza
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroBench)
                    .weight(0.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.categoria),
                    contentDescription = "Categoria",
                    modifier = Modifier
                        .size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    ejercicio.categoria,
                    color = rojoBench,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(negroBench)
                    .weight(0.5f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tipo_fuerza),
                    contentDescription = "Tipo fuerza",
                    modifier = Modifier
                        .size(35.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    ejercicio.tipo_fuerza,
                    color = rojoBench,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        // Columna de musculo principal y secundario
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(negroBench)
                .weight(0.5f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showInfoDialog by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 10.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = { showInfoDialog = !showInfoDialog },
                    modifier = Modifier.size(28.dp) // Tamaño más compacto
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = rojoBench,
                        modifier = Modifier.size(18.dp) // Tamaño del ícono más pequeño
                    )
                }
            }
            InfoDialog(
                title = "Iconos",
                showDialog = showInfoDialog,
                onDismiss = { showInfoDialog = false },
                cuerpo = { InfoIconos() }
            )
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.musculo_principal),
                    contentDescription = "musculo principal",
                    modifier = Modifier
                        .size(25.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    ejercicio.musculo_principal, color = rojoBench,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(50.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.musculo_secundario),
                    contentDescription = "musculo secundario",
                    modifier = Modifier
                        .size(25.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    ejercicio.musculo_secundario, color = rojoBench,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun FilaEquipamiento(equipamiento: String) {
    Spacer(Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroBench),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.equipamiento),
                contentDescription = "equipamiento",
                modifier = Modifier
                    .size(30.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                equipamiento, color = rojoBench,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FilaGif(urlGIF: String) {
    Spacer(Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
    ) {
        AdaptiveGifRow(urlGIF)
    }
    Spacer(Modifier.height(10.dp))
}

@Composable
fun FilaInstrucciones(instrucciones: String) {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(negroBench)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Instrucciones", color = rojoBench,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                instrucciones, color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}



@Composable
fun InfoIconos() {
    Column(
        modifier = Modifier.height(300.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.categoria),
                contentDescription = "categoría",
                modifier = Modifier
                    .size(25.dp)
            )
            Text(
                "Categoría", color = rojoBench,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.tipo_fuerza),
                contentDescription = "movimiento",
                modifier = Modifier
                    .size(40.dp)
            )
            Text(
                "Movimiento", color = rojoBench,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.musculo_principal),
                contentDescription = "musculo_principal",
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                "Músculo Principal", color = rojoBench,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.musculo_secundario),
                contentDescription = "musculo_secundario",
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                "Músculo Secundario", color = rojoBench,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.equipamiento),
                contentDescription = "equipamiento",
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                "Equipamiento", color = rojoBench,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
