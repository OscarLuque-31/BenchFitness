package com.oscar.benchfitness.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.animations.SplashScreen
import com.oscar.benchfitness.screens.auth.LoginScreen
import com.oscar.benchfitness.screens.auth.RegistroScreen
import com.oscar.benchfitness.screens.datos.DatosScreen
import com.oscar.benchfitness.screens.main.MainContainer
import com.oscar.benchfitness.screens.start.InicioScreen
import com.oscar.benchfitness.screens.start.LogoScreen
import com.oscar.benchfitness.viewModels.auth.AuthViewModel
import com.oscar.benchfitness.viewModels.auth.LoginViewModel
import com.oscar.benchfitness.viewModels.auth.RegistroViewModel
import com.oscar.benchfitness.viewModels.datos.DatosViewModel

@Composable
fun AppNavegation(auth: FirebaseAuth, db: FirebaseFirestore) {
    // Controlador de la navegación principal
    val navController = rememberNavController()

    // ViewModel que controla parte de la autenticación
    val authViewModel = remember { AuthViewModel(auth, db) }

    // Variable para mostrar la pantalla de carga
    var showSplash by remember { mutableStateOf(true) }

    // Navega automáticamente a la pantalla principal si hay un usuario autenticado
    LaunchedEffect(true) {
        authViewModel.verificarEstado()
    }

    // Si es true se muestra la pantalla de carga
    if (showSplash) {
        SplashScreen { showSplash = false }
        return
    }

    // Decide la pantalla inicial según si los datos están completos
    val startDestination = when {
        !authViewModel.isAuthenticated -> Inicio.route
        authViewModel.datosCompletados -> Main.route
        else -> Datos.route
    }

    // NavHost principal de la aplicación
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Logo.route) { LogoScreen() }
        composable(Inicio.route) { InicioScreen(navController) }
        composable(Login.route) { LoginScreen(navController, viewModel = LoginViewModel(auth, db)) }
        composable(Registro.route) {
            RegistroScreen(
                navController,
                viewModel = RegistroViewModel(auth, db)
            )
        }
        composable(Datos.route) { DatosScreen(navController, viewModel = DatosViewModel(auth, db)) }
        composable(Main.route) {
            MainContainer(auth, db, navController, authViewModel)
        }
    }
}