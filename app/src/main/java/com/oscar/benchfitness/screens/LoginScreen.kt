package com.oscar.benchfitness.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Principal
import com.oscar.benchfitness.navegation.Registro
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.widgets.GlobalButton
import com.oscar.benchfitness.widgets.GlobalTextField


@Composable
fun LoginScreen(navController: NavController, auth: FirebaseAuth) {
    val context = LocalContext.current // Obtener el contexto para Toast

    var email: String by remember { mutableStateOf("") }
    var password: String by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0) // Elimina el padding predeterminado
    ) { paddingValues ->
        LoginBodyContent(
            navController = navController,
            email = email,
            password = password,
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onLoginClick = {
                val (isValid, errorMessage) = validateLoginFields(email, password)
                if (!isValid) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                navController.navigate(Principal)
                            } else {
                                Toast.makeText(
                                    context,
                                    "No se ha encontrado el usuario",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = rojoBench)
                .imePadding()
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun LoginBodyContent(
    navController: NavController,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Barra de arriba
        LoginTopBar(navController)
        Spacer(modifier = Modifier.weight(1f))
        // Datos a recoger en el login
        LoginDatos(navController, email, password, onEmailChange, onPasswordChange, onLoginClick)
    }
}

@Composable
fun LoginTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            navController.navigate(route = Inicio) {
                popUpTo<Inicio> {
                    inclusive = true
                }
            }
        }) {
            Icon(
                tint = negroBench,
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Flecha hacia atrás"
            )
        }

        TextButton(onClick = {
            navController.navigate(route = Registro) {
                popUpTo<Registro> { inclusive = true }
            }
        }) {
            Text(
                "Registro",
                fontSize = 18.sp,
                color = negroBench,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
    }
}


@Composable
fun LoginDatos(
    navController: NavController,
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            "Inicio de sesión",
            modifier = Modifier.padding(start = 30.dp, bottom = 20.dp),
            style = MaterialTheme.typography.bodyLarge, fontSize = 24.sp,
            color = negroBench
        )
        Column(
            modifier = Modifier
                .height(550.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = negroBench)
        ) {
            LoginTextFields(email, password, onEmailChange, onPasswordChange, onLoginClick)
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .background(color = negroClaroBench)
            )
            LoginButtonGoogle()
        }
    }
}

@Composable
fun LoginTextFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalTextField(
            nombre = "Correo electrónico",
            text = email,
            onValueChange = onEmailChange,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        GlobalTextField(
            nombre = "Contraseña",
            text = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "¿Contraseña olvidada?",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 10.sp,
            color = Color.White,
            modifier = Modifier.width(310.dp),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(36.dp))
        GlobalButton(
            "Iniciar sesión", rojoBench,
            modifier = Modifier
                .width(310.dp)
                .height(50.dp),
            colorText = Color.White,
        ) { onLoginClick() }
    }
}


@Composable
fun LoginButtonGoogle() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalButton(
            "Continuar con Google",
            Color.White,
            negroBench,
            modifier = Modifier
                .width(310.dp)
                .height(50.dp),
            { })
    }
}


// Función para validar los campos de inicio de sesión
fun validateLoginFields(email: String, password: String): Pair<Boolean, String> {
    return when {
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(
            false,
            "Ingresa un email válido."
        )
        password.length < 6 -> Pair(false, "La contraseña debe tener al menos 6 caracteres.")
        else -> Pair(true, "")
    }
}