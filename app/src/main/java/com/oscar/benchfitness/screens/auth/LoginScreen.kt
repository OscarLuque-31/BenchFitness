package com.oscar.benchfitness.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Mostrar snackbar si hay error
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LoginBodyContent(
        navController = navController,
        email = viewModel.email,
        password = viewModel.password,
        onEmailChange = { viewModel.email = it },
        onPasswordChange = { viewModel.password = it },
        onLoginClick = {
            viewModel.loginUser(navController) { errorMessage ->
                viewModel.errorMessage = errorMessage
            }
        },
        onGoogleLoginClick = {
            viewModel.loginWithGoogle(
                context,
                navController = navController,
                onFailure = { error ->
                    viewModel.errorMessage = error
                })
        },
        modifier = Modifier
            .fillMaxSize()
            .background(rojoBench)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        snackbarHostState = snackbarHostState
    )
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
    snackbarHostState: SnackbarHostState, // 👈 nuevo parámetro
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        LoginTopBar(navController)

        // Snackbar entre topbar y contenido
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            snackbar = { snackbarData ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = negroOscuroBench)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        color = rojoBench,
                        fontSize = 16.sp
                    )
                }
            }
        )

        LoginDatos(
            email,
            password,
            onEmailChange,
            onPasswordChange,
            onLoginClick,
            onGoogleLoginClick,
            modifier = Modifier.weight(0.7f)
        )
    }
}


@Composable
fun LoginTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            navController.navigate(route = Inicio.route) {
                popUpTo(Inicio.route) {
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
            navController.navigate(route = Registro.route) {
                popUpTo(Registro.route) { inclusive = true }
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
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
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
            .padding(horizontal = 30.dp, vertical = 30.dp), // 👈 Aquí agregas el padding lateral
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalTextField(
            nombre = "Correo electrónico",
            text = email,
            onValueChange = onEmailChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(25.dp))
        GlobalTextField(
            nombre = "Contraseña",
            text = password,
            onValueChange = onPasswordChange,
            isPassword = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "¿Contraseña olvidada?",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 13.sp,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(), // alineado por padding del padre
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(36.dp))
        GlobalButton(
            "Iniciar sesión", rojoBench,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
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
            .padding(horizontal = 30.dp, vertical = 30.dp),
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
                .fillMaxWidth()
                .height(55.dp)
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
