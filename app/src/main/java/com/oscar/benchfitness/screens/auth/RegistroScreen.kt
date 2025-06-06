package com.oscar.benchfitness.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.animations.LoadingScreenCircularProgress
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.navegation.Datos
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Login
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.auth.RegistroViewModel

@Composable
fun RegistroScreen(navController: NavController, viewModel: RegistroViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Muestra el snackbar si hay error
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    if (viewModel.isLoading) {
        LoadingScreenCircularProgress()
    } else {
        RegisterBodyContent(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .background(color = rojoBench)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            snackbarHostState
        )
    }
}

@Composable
fun RegisterBodyContent(
    navController: NavController,
    viewModel: RegistroViewModel,
    modifier: Modifier,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = modifier
    ) {
        RegisterTopBar(navController)
        Spacer(modifier = Modifier.weight(0.3f))
        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            snackbar = { snackbarData ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(negroOscuroBench)
                        .padding(12.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        color = rojoBench,
                        fontSize = 16.sp
                    )
                }
            }
        )
        // Datos a recoger en el registro
        RegisterDatos(navController, viewModel)
    }
}

@Composable
fun RegisterTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
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
            navController.navigate(route = Login.route) {
                popUpTo(Login.route) { inclusive = true }
            }
        }) {
            Text(
                "Iniciar sesión",
                fontSize = 18.sp,
                color = negroBench,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
    }
}

@Composable
fun RegisterDatos(navController: NavController, viewModel: RegistroViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            "Registro",
            modifier = Modifier.padding(start = 30.dp, bottom = 20.dp),
            style = MaterialTheme.typography.bodyLarge, fontSize = 24.sp,
            color = negroBench
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = negroBench),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterTextFields(navController, viewModel)
        }
    }
}

@Composable
fun RegisterTextFields(navController: NavController, viewModel: RegistroViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalTextField(
            nombre = "Nombre de usuario",
            text = viewModel.username,
            onValueChange = { viewModel.username = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench
        )
        Spacer(modifier = Modifier.height(25.dp))
        GlobalTextField(
            nombre = "Email",
            text = viewModel.email,
            onValueChange = { viewModel.email = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench
        )
        Spacer(modifier = Modifier.height(25.dp))
        GlobalTextField(
            nombre = "Contraseña",
            text = viewModel.password,
            onValueChange = { viewModel.password = it },
            isPassword = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench
        )
        Spacer(modifier = Modifier.height(25.dp))
        GlobalTextField(
            nombre = "Repetir contraseña",
            text = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            isPassword = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            backgroundColor = Color.White,
            colorText = negroOscuroBench
        )
        Spacer(modifier = Modifier.height(25.dp))
        ConfirmarTerminosYCondiciones(
            checked = viewModel.acceptTerms,
            onCheckedChange = { viewModel.acceptTerms = it })
        Spacer(modifier = Modifier.height(25.dp))
        GlobalButton(
            "Registrar",
            rojoBench,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colorText = Color.White
        ) {
            // Registra el usuario en base de datos
            viewModel.registerUser(
                onSuccess = {
                    // Navega a la pantalla de datos
                    navController.navigate(Datos.route) {
                        popUpTo(Inicio.route) {
                            inclusive = true
                        }
                    }
                },
                onFailure = { error -> viewModel.errorMessage = error }
            )
        }
    }
}

@Composable
fun ConfirmarTerminosYCondiciones(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = rojoBench,
                uncheckedColor = negroClaroBench,
                checkmarkColor = Color.White
            )
        )
        Text(
            "Confirmo que he leído y acepto los términos y condiciones de la aplicación, incluyendo las políticas de privacidad y el uso de datos.",
            fontSize = 12.sp,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}


