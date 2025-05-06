package com.oscar.benchfitness.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.ui.theme.negroClaroBench

@Composable
fun LogoScreen(navController: NavController){
    Scaffold { paddingValues ->
        LogoBodyContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun LogoBodyContent(navController: NavController, modifier: Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroClaroBench)
    ) {
        // Columna principal centrada
        ColumnaPrincipalLogo(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f) // Ocupa el espacio disponible
        )
    }
}
@Composable
fun ColumnaPrincipalLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center, // Centra verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_marca),
            contentDescription = "Logo aplicación"
        )
    }
}


@Preview()
@Composable
fun LogoPreview(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroClaroBench)
    ) {
        // Columna principal centrada
        ColumnaPrincipalLogo(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f) // Ocupa el espacio disponible
        )
    }}