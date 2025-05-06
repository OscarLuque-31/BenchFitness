package com.oscar.benchfitness.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.Login
import com.oscar.benchfitness.navegation.Registro
import com.oscar.benchfitness.ui.theme.*

@Composable
fun InicioScreen(navController: NavController) {
    Scaffold { paddingValues ->
        InicioBodyContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun InicioBodyContent(navController: NavController, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
    ) {
        // Columna principal centrada
        ColumnaPrincipal(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.67f) // Ocupa el espacio disponible
            ,
            modifierImagen = Modifier.size(150.dp)
        )

        // Contenedor de bienvenida en la parte inferior
        ContenedorBienvenida(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.33f),
            navController
        )
    }
}


@Composable
fun ColumnaPrincipal(modifier: Modifier, modifierImagen: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center, // Centra verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_marca),
            contentDescription = "Logo aplicación",
            modifier = modifierImagen
        )
    }
}

@Composable
fun ContenedorBienvenida(modifier: Modifier = Modifier, navController: NavController) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .fillMaxWidth()
            .background(color = rojoBench)
            .padding(24.dp) // Espaciado interno
    ) {
        Text(
            "Bienvenido",
            fontSize = 24.sp,
            color = negroBench,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre textos
        Text(
            "Bench. te ofrece la herramienta para construir tu mejor versión, paso a paso, entrenamiento a entrenamiento. Cada sesión es una oportunidad para superarte y alcanzar el siguiente nivel.",
            fontSize = 16.sp,
            color = negroBench,
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMedium
        )
        BotonesBienvenida(navController)
    }
}

@Composable
fun BotonesBienvenida(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(45.dp)
    ) {
        Button(onClick = {
            navController.navigate(route = Login.route)
        },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = negroBench,
                contentColor = Color.White
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)) {
            Text(
                "Iniciar sesión",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp
            )
        }
        Button(onClick = {
            navController.navigate(route = Registro.route)
        },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = negroBench
            ),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)) {
            Text(
                "Registrarse",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp
            )
        }
    }
}
