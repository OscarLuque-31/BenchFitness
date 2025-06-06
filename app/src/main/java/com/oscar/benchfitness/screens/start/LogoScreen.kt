package com.oscar.benchfitness.screens.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.oscar.benchfitness.R
import com.oscar.benchfitness.ui.theme.negroClaroBench

@Composable
fun LogoScreen() {
    LogoBodyContent()
}

@Composable
fun LogoBodyContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroClaroBench)
    ) {
        ColumnaPrincipalLogo(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.65f)
        )
    }
}

@Composable
fun ColumnaPrincipalLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_marca),
            contentDescription = "Logo aplicación"
        )
    }
}

