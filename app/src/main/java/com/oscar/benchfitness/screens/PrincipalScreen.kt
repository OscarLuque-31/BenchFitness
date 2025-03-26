package com.oscar.benchfitness.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.R
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.DatosViewModel
import com.oscar.benchfitness.viewModels.PrincipalViewModel

@Composable
fun PrincipalScreen(navController: NavController, auth: FirebaseAuth, db: FirebaseFirestore) {
    val viewModel: PrincipalViewModel = viewModel();

    LaunchedEffect(Unit) {
        viewModel.cargarNombreUsuario(auth, db)
    }

    Scaffold { paddingValues ->
        PrincipalBodyContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel
        )
    }
}

@Composable
fun PrincipalBodyContent(navController: NavController, modifier: Modifier, viewModel: PrincipalViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
    ) {
        GlobalHeader("Bienvenido ${viewModel.nombre}")
    }
}

@Composable
fun GlobalHeader(text: String) {
    LogoBenchFitness()
    CabeceraBenchFitness(text)
}

@Composable
fun LogoBenchFitness() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End) {
        Image(
            painter = painterResource(id = R.drawable.logo_marca),

            contentDescription = "Logo aplicación",
            modifier = Modifier.size(90.dp)
        )
    }
}

@Composable
fun CabeceraBenchFitness(text: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)) {
        Text(
            text,
            modifier = Modifier.padding(bottom = 10.dp),
            style = MaterialTheme.typography.bodySmall, fontSize = 24.sp,
            color = Color.White
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(rojoBench)
        )
    }
}
