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
import com.oscar.benchfitness.screens.DatosScreen
import com.oscar.benchfitness.screens.InicioScreen
import com.oscar.benchfitness.screens.LoginScreen
import com.oscar.benchfitness.screens.LogoScreen
import com.oscar.benchfitness.screens.PrincipalScreen
import com.oscar.benchfitness.screens.RegistroScreen

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
        datosCompletados -> Principal
        else -> Datos
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable<Logo> { LogoScreen(navController) }
        composable<Inicio> { InicioScreen(navController) }
        composable<Login> { LoginScreen(navController, auth) }
        composable<Registro> { RegistroScreen(navController, auth, db) }
        composable<Datos> { DatosScreen(navController, db, auth) }
        composable<Principal> { PrincipalScreen(navController, auth, db) }
        composable<Ejercicios> {}
        composable<Ejercicio> {}
        composable<Calculos> {}
        composable<Perfil> {}
    }

}