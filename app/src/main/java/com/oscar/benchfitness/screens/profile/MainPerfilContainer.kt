package com.oscar.benchfitness.screens.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.animations.LoadingScreenCircularProgress
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.navegation.Perfil
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.viewModels.auth.AuthViewModel
import com.oscar.benchfitness.viewModels.profile.PerfilViewModel


@Composable
fun MainPerfilContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val innerNavController = rememberNavController()


    Scaffold(containerColor = negroBench, topBar = {
        GlobalHeader("Perfil")
    }) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = Perfil.route,
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
            )

        ) {
            composable(Perfil.route) {
                val perfilViewModel = remember { PerfilViewModel(auth, db, authViewModel) }

                LaunchedEffect(Unit) {
                    perfilViewModel.cargarPerfilUsuario()
                }

                if (perfilViewModel.isLoading) {
                    LoadingScreenCircularProgress()
                } else {
                    PerfilScreen(navController = navController, viewModel = perfilViewModel)
                }
            }
        }
    }

}