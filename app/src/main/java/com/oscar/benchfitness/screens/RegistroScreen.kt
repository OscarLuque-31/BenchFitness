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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.oscar.benchfitness.data.FirebaseData
import com.oscar.benchfitness.navegation.Datos
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.navegation.Login
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.widgets.DatePickerField
import com.oscar.benchfitness.widgets.GlobalButton
import com.oscar.benchfitness.widgets.GlobalTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun RegistroScreen(navController: NavController, auth: FirebaseAuth, db: FirebaseFirestore) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = rojoBench),
        contentWindowInsets = WindowInsets(0)
    ) { paddingValues ->
        RegisterBodyContent(
            navController = navController,
            auth = auth,
            db = db,
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
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    modifier: Modifier
) {


    Column(
        modifier = modifier
    ) {
        // Barra de arriba
        RegisterTopBar(navController)
        Spacer(modifier = Modifier.weight(1f))
        // Datos a recoger en el login
        RegisterDatos(navController, auth, db)
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
fun RegisterDatos(navController: NavController, auth: FirebaseAuth, db: FirebaseFirestore) {
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
            RegisterTextFields(navController, auth, db)
        }
    }
}

@Composable
fun RegisterTextFields(navController: NavController, auth: FirebaseAuth, db: FirebaseFirestore) {
    val context = LocalContext.current  // Obtener contexto para Toast

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlobalTextField(
            nombre = "Nombre de usuario",
            text = username,
            onValueChange = { username = it },
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        GlobalTextField(
            nombre = "Email",
            text = email,
            onValueChange = { email = it },
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        DatePickerField(onDateSelected = { birthday = it })
        Spacer(modifier = Modifier.height(25.dp))

        GlobalTextField(
            nombre = "Contraseña",
            text = password,
            onValueChange = { password = it },
            isPassword = true,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        GlobalTextField(
            nombre = "Repetir contraseña",
            text = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true,
            modifier = Modifier
                .width(310.dp)
                .height(55.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))

        ConfirmarTerminosYCondiciones(checked = acceptTerms, onCheckedChange = { acceptTerms = it })
        Spacer(modifier = Modifier.height(25.dp))

        GlobalButton(
            "Registrar",
            rojoBench,
            modifier = Modifier
                .width(310.dp)
                .height(50.dp),
            colorText = Color.White
        ) {
            val (isValid, errorMessage) = validateFields(
                username,
                email,
                password,
                confirmPassword,
                birthday,
                acceptTerms
            )

            if (!isValid) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                FirebaseData().registerUser(
                    auth = auth,
                    db = db,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    username = username,
                    birthdate = birthday,
                    acceptTerms = acceptTerms,
                    onSuccess = { navController.navigate(Datos) },
                    onFailure = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            }
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



fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun validateFields(
    username: String, email: String, password: String, confirmPassword: String,
    birthday: String, acceptTerms: Boolean
): Pair<Boolean, String> {
    return when {
        username.isBlank() -> Pair(false, "El nombre de usuario no puede estar vacío.")
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(
            false,
            "Ingresa un email válido."
        )

        password.length < 6 -> Pair(false, "La contraseña debe tener al menos 6 caracteres.")
        password != confirmPassword -> Pair(false, "Las contraseñas no coinciden.")
        birthday.isBlank() -> Pair(false, "Selecciona una fecha de nacimiento.")
        !acceptTerms -> Pair(false, "Debes aceptar los términos y condiciones.")
        else -> Pair(true, "")
    }
}

