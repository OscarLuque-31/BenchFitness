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

    if (showSplash) {
        SplashScreen { showSplash = false }
        return
    }

    // Decide la pantalla inicial según si los datos están completos
    val startDestination = when {
        currentUser == null -> Inicio
        datosCompletados -> Main
        else -> Datos
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable<Logo> { LogoScreen(navController) }
        composable<Inicio> { InicioScreen(navController) }
        composable<Login> { LoginScreen(navController, viewModel = LoginViewModel(auth, db)) }
        composable<Registro> {
            RegistroScreen(
                navController,
                viewModel = RegistroViewModel(auth, db)
            )
        }
        composable<Datos> { DatosScreen(navController, viewModel = DatosViewModel(auth, db)) }
        composable<Main> {
            MainContainer(navController = navController, auth, db)
        }
    }
}