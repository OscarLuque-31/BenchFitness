package com.oscar.benchfitness.screens.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.navegation.Rutina
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench

@Composable
fun RutinasScreen(navController: NavController) {
    Column (modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
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
            .background(
                negroOscuroBench
            ).clickable {
                navController.navigate(Rutina)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("Crear rutina", color = rojoBench, fontWeight = FontWeight.Normal)
        Image(
            painter = painterResource(id = R.drawable.anadir),
            contentDescription = "añadir",
            modifier = Modifier.size(50.dp)
        )
    }
}