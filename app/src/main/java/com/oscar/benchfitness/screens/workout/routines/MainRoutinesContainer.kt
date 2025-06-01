package com.oscar.benchfitness.screens.workout.routines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.models.routines.Routine
import com.oscar.benchfitness.navegation.CrearRutina
import com.oscar.benchfitness.navegation.Rutina
import com.oscar.benchfitness.navegation.Rutinas
import com.oscar.benchfitness.viewModels.workout.CrearRutinaViewModel
import com.oscar.benchfitness.viewModels.workout.RutinaViewModel
import com.oscar.benchfitness.viewModels.workout.RutinasViewModel

@Composable
fun MainRoutinesContainer(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
) {
    val navController = rememberNavController()


    NavHost(navController, startDestination = Rutinas.route) {

        composable(Rutinas.route) {
            val rutinaViewModel = remember { RutinasViewModel(auth, db) }

            LaunchedEffect(Unit) {
                rutinaViewModel.obtenerRutinas()
            }

            RutinasScreen(navController, rutinaViewModel)
        }
        composable(CrearRutina.route) {

            val crearRutinaViewModel = remember { CrearRutinaViewModel(auth, db) }
            CrearRutinaScreen(navController, crearRutinaViewModel)

        }
        composable(Rutina.route) {

            val rutinaViewModel = remember { RutinaViewModel(auth, db) }

            val rutina = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Routine>("rutina")


            if (rutina != null) {
                RutinaScreen(navController, rutinaViewModel, rutina)
            }

        }
    }

}
