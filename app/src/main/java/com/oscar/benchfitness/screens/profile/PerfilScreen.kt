package com.oscar.benchfitness.screens.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Login
import com.oscar.benchfitness.navegation.Perfil
import com.oscar.benchfitness.screens.start.InicioScreen
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.profile.PerfilViewModel

@Composable
fun PerfilScreen(navController: NavController, viewModel: PerfilViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cerrar sesión?",
                    color = rojoBench,
                    fontSize = 20.sp
                )
            },
            confirmButton = {
                Text(
                    text = "Sí",
                    color = rojoBench,
                    modifier = Modifier
                        .clickable {
                            showDialog = false
                            viewModel.cerrarSesion()
                            navController.navigate(Inicio.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        .padding(10.dp),
                    fontSize = 16.sp
                )
            },
            dismissButton = {
                Text(
                    text = "Cancelar",
                    color = Color.White,
                    modifier = Modifier
                        .clickable { showDialog = false }
                        .padding(10.dp),
                    fontSize = 16.sp
                )
            },
            containerColor = negroBench,
        )
    }

    Column(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
        ColumnaNombreRutinas(viewModel)
        Spacer(Modifier.height(20.dp))
        ColumnaDatos(modifier = Modifier.weight(1f), viewModel)
        Spacer(Modifier.height(20.dp))
        GlobalButton(
            "Cerrar sesión",
            backgroundColor = rojoBench,
            colorText = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            showDialog = true
        }
    }
}



@Composable
fun ColumnaNombreRutinas(viewModel: PerfilViewModel) {
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                negroOscuroBench
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            viewModel.usuario.username,
            color = rojoBench,
            fontSize = 34.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.rutina),
                contentDescription = "Numero de rutinas",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(20.dp))
            Text(
                if (viewModel.numRutinas.toInt() == 1) "${viewModel.numRutinas} rutina" else "${viewModel.numRutinas} rutinas",
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ColumnaDatos(modifier: Modifier, viewModel: PerfilViewModel) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                negroOscuroBench
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Datos", color = rojoBench, fontSize = 24.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            DatosPerfil(viewModel)
            if (!viewModel.isGoogleUser) {
                GlobalButton(
                    text = "Cambiar contraseña",
                    colorText = Color.White,
                    backgroundColor = negroBench,
                    modifier = Modifier.fillMaxWidth()
                ) { }
            }
        }
    }
}


@Composable
fun DatosPerfil(viewModel: PerfilViewModel) {
    Column {
        Text(
            "Correo electrónico",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            viewModel.usuario.email,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
    Column {
        Text(
            "Fecha de nacimiento",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            viewModel.usuario.birthday,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
    Column {
        Text(
            "Objetivo",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            viewModel.usuario.objetivo,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
    Column {
        Text(
            "Altura",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            "${viewModel.usuario.altura} cm",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
}