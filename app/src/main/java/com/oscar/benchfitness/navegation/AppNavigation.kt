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
import com.oscar.benchfitness.screens.InicioScreen
import com.oscar.benchfitness.screens.LogoScreen
import com.oscar.benchfitness.screens.auth.LoginScreen
import com.oscar.benchfitness.screens.auth.RegistroScreen
import com.oscar.benchfitness.screens.datos.DatosScreen
import com.oscar.benchfitness.screens.main.MainContainer
import com.oscar.benchfitness.viewModels.auth.LoginViewModel
import com.oscar.benchfitness.viewModels.auth.RegistroViewModel
import com.oscar.benchfitness.viewModels.datos.DatosViewModel
import com.oscar.benchfitness.viewModels.statistics.StatisticsScreen

@Composable
fun AppNavegation(auth: FirebaseAuth, db: FirebaseFirestore) {
    val navController = rememberNavController()

    val currentUser = auth.currentUser
    var datosCompletados by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(true) }
    var showSplash by remember { mutableStateOf(true) }

    // Navega automáticamente a la pantalla principal si hay un usuario autenticado
    LaunchedEffect(currentUser) {

        // Si el usuario no existe se cierra la sesión
        currentUser?.reload()?.addOnCompleteListener { task ->
            if (task.isCanceled) {
                auth.signOut()
            }
        }

        // Si el usuario no es nulo comprueba que existan sus datos
        if (currentUser != null) {
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    datosCompletados = document.getBoolean("datosCompletados") == true
                    isChecking = false
                }
                .addOnFailureListener {
                    isChecking = false
                }
        } else {
            isChecking = false
        }
    }

    // Si es true se muestra la pantalla de carga
    if (showSplash) {
        SplashScreen { showSplash = false }
        return
    }

    // Decide la pantalla inicial según si los datos están completos
    val startDestination = when {
        currentUser == null -> Inicio.route
        datosCompletados -> Main.route
        else -> Datos.route
    }

    // NavHost principal
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Logo.route) { LogoScreen(navController) }
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
            MainContainer(auth, db)
        }
    }
}