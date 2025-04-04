package com.oscar.benchfitness

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.oscar.benchfitness.navegation.AppNavegation
import com.oscar.benchfitness.services.RetrofitClient
import com.oscar.benchfitness.ui.theme.BenchfitnessTheme

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()

        // Inicializa Retrofit con el contexto
        RetrofitClient.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            BenchfitnessTheme {
               AppNavegation(auth,db)
            }
        }
    }
}

