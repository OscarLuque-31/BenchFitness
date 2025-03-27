package com.oscar.benchfitness.screens.auth

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.DatePickerField
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.navegation.Datos
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Login
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.auth.RegistroViewModel


@Composable
fun RegistroScreen(navController: NavController, viewModel: RegistroViewModel) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = rojoBench),
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        RegisterBodyContent(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = rojoBench)
                .verticalScroll(rememberScrollState())
                .imePadding()
        )
    }
}


@Composable
fun RegisterBodyContent(
    navController: NavController,
    viewModel: RegistroViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Barra de arriba
        RegisterTopBar(navController)
        Spacer(modifier = Modifier.weight(1f))
        // Datos a recoger en el login
        RegisterDatos(navController, viewModel)
    }
}

@Composable
fun RegisterTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
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
            navController.navigate(route = Login) {
                popUpTo<Login> { inclusive = true }
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
                .height(600.dp)
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
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalTextField(
            nombre = "Nombre de usuario",
            text = viewModel.username.value,
            onValueChange = { viewModel.username.value = it },
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        GlobalTextField(
            nombre = "Email",
            text = viewModel.email.value,
            onValueChange = { viewModel.email.value = it },
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        DatePickerField(onDateSelected = { viewModel.birthday.value = it })
        Spacer(modifier = Modifier.height(25.dp))

        GlobalTextField(
            nombre = "Contraseña",
            text = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            isPassword = true,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        GlobalTextField(
            nombre = "Repetir contraseña",
            text = viewModel.confirmPassword.value,
            onValueChange = { viewModel.confirmPassword.value = it },
            isPassword = true,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        ConfirmarTerminosYCondiciones(
            checked = viewModel.acceptTerms.value,
            onCheckedChange = { viewModel.acceptTerms.value = it })
        Spacer(modifier = Modifier.height(25.dp))

        GlobalButton(
            "Registrar",
            rojoBench,
            modifier = Modifier
                .width(310.dp)
                .height(50.dp),
            colorText = Color.White
        ) {
            viewModel.registerUser(
                onSuccess = { navController.navigate(Datos) },
                onFailure = { error -> Toast.makeText(context, error, Toast.LENGTH_SHORT).show() }
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
            fontSize = 10.sp,
            color = Color.White,
            textAlign = TextAlign.Justify
        )
    }
}


