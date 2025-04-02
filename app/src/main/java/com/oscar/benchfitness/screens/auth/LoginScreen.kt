package com.oscar.benchfitness.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Registro
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.auth.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        LoginBodyContent(
            navController = navController,
            email = viewModel.email,
            password = viewModel.password,
            onEmailChange = { viewModel.email = it },
            onPasswordChange = { viewModel.password = it },
            onLoginClick = {
                viewModel.loginUser(navController) { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            },
            onGoogleLoginClick = {
                scope.launch {
                    viewModel.loginWithGoogle(context, navController, onFailure = { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    })
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
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Barra de arriba
        LoginTopBar(navController)
        Spacer(modifier = Modifier.weight(1f))
        // Datos a recoger en el login
        LoginDatos(
            email,
            password,
            onEmailChange,
            onPasswordChange,
            onLoginClick,
            onGoogleLoginClick
        )
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
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit
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
            LoginButtonGoogle(onGoogleLoginClick = onGoogleLoginClick)
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
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench
        )
        Spacer(modifier = Modifier.height(25.dp))
        GlobalTextField(
            nombre = "Contraseña",
            text = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench
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
fun LoginButtonGoogle(
    onGoogleLoginClick: () -> Unit  // Recibimos el callback
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onGoogleLoginClick,  // Usamos el callback
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = negroBench
            ),
            modifier = Modifier
                .width(310.dp)
                .height(50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_google),
                contentDescription = "Logo de Google",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text(
                "Continuar con Google",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp
            )
        }
    }
}
