package com.oscar.benchfitness.screens.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.oscar.benchfitness.R
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.navegation.Inicio
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.validarAltura
import com.oscar.benchfitness.utils.validarObjetivo
import com.oscar.benchfitness.viewModels.profile.PerfilViewModel

@Composable
fun PerfilScreen(navController: NavController, viewModel: PerfilViewModel) {

    if (viewModel.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showLogoutDialog = !viewModel.showLogoutDialog },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas cerrar sesión?",
                    color = rojoBench,
                    fontSize = 20.sp
                )
            },
            confirmButton = {
                Text(
                    text = "Sí",
                    color = rojoBench,
                    modifier = Modifier
                        .clickable {
                            viewModel.showLogoutDialog = !viewModel.showLogoutDialog
                            viewModel.cerrarSesion()
                            navController.navigate(Inicio.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        .padding(10.dp),
                    fontSize = 16.sp
                )
            },
            dismissButton = {
                Text(
                    text = "Cancelar",
                    color = Color.White,
                    modifier = Modifier
                        .clickable { viewModel.showLogoutDialog = !viewModel.showLogoutDialog }
                        .padding(10.dp),
                    fontSize = 16.sp
                )
            },
            containerColor = negroBench,
        )
    }

    Column(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
        ColumnaNombreRutinas(viewModel)
        Spacer(Modifier.height(20.dp))
        ColumnaDatos(modifier = Modifier.weight(1f), viewModel)
        Spacer(Modifier.height(20.dp))
        GlobalButton(
            "Cerrar sesión",
            backgroundColor = rojoBench,
            colorText = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            viewModel.showLogoutDialog = !viewModel.showLogoutDialog
        }
    }
}


@Composable
fun ColumnaNombreRutinas(viewModel: PerfilViewModel) {
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                negroOscuroBench
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            viewModel.usuario.username,
            color = rojoBench,
            fontSize = 34.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.rutina),
                contentDescription = "Numero de rutinas",
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(20.dp))
            Text(
                if (viewModel.numRutinas.toInt() == 1) "${viewModel.numRutinas} rutina" else "${viewModel.numRutinas} rutinas",
                color = Color.White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ColumnaDatos(modifier: Modifier, viewModel: PerfilViewModel) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                negroOscuroBench
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Datos", color = rojoBench, fontSize = 24.sp, fontWeight = FontWeight.Medium)
            }
            DialogEditarDato(viewModel)
            DatosPerfil(viewModel)
            if (!viewModel.isGoogleUser) {
                GlobalButton(
                    text = "Cambiar contraseña",
                    colorText = Color.White,
                    backgroundColor = negroBench,
                    modifier = Modifier.fillMaxWidth()
                ) { viewModel.showPasswordDialog = !viewModel.showPasswordDialog }
            }

            if (viewModel.showPasswordDialog) {
                CambiarPasswordDialog(
                    onDismiss = { viewModel.showPasswordDialog = !viewModel.showPasswordDialog },
                    perfilViewModel = viewModel
                )
            }
            if (viewModel.showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.showSuccessDialog = !viewModel.showSuccessDialog
                    },
                    title = { Text("Contraseña cambiada", color = Color.White) },
                    text = {
                        Text(
                            "Tu contraseña ha sido cambiada exitosamente.",
                            color = rojoBench
                        )
                    },
                    confirmButton = {
                        Text(
                            "Aceptar",
                            color = rojoBench,
                            modifier = Modifier
                                .clickable {
                                    viewModel.showSuccessDialog = !viewModel.showSuccessDialog
                                }
                                .padding(10.dp)
                        )
                    },
                    containerColor = negroBench
                )
            }

        }
    }
}


@Composable
fun DatosPerfil(viewModel: PerfilViewModel) {
    Column {
        Text(
            "Correo electrónico",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            viewModel.usuario.email,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
    Column {
        Text(
            "Fecha de nacimiento",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            viewModel.usuario.birthday,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
    Column {
        Text(
            "Objetivo",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                viewModel.usuario.objetivo,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            IconButton(onClick = { viewModel.editObjetivo = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar objetivo", tint = rojoBench)
            }
        }
    }
    Column {
        Text(
            "Altura",
            color = rojoBench,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${viewModel.usuario.altura} cm",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            IconButton(onClick = { viewModel.editAltura = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar altura", tint = rojoBench)
            }
        }
    }
}


@Composable
fun DialogEditarDato(viewModel: PerfilViewModel) {
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val opcionesObjetivoFit: List<String> = listOf(
        "Perder peso",
        "Mantener peso",
        "Masa muscular"
    )

    if (viewModel.editObjetivo || viewModel.editAltura) {
        AlertDialog(
            onDismissRequest = {
                viewModel.editObjetivo = false
                viewModel.editAltura = false
            },
            title = {
                Text("Editar información", color = Color.White, fontSize = 20.sp)
            },
            text = {
                Column {
                    if (viewModel.editObjetivo) {
                        Text("Objetivo", color = rojoBench)
                        Spacer(modifier = Modifier.height(8.dp))
                        GlobalDropDownMenu(
                            viewModel.newObjetivo,
                            opcionesObjetivoFit,
                            onValueChange = { viewModel.newObjetivo = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            backgroundColor = negroOscuroBench,
                            colorText = rojoBench
                        )
                    }

                    if (viewModel.editAltura) {
                        Text("Altura", color = rojoBench)
                        Spacer(modifier = Modifier.height(8.dp))
                        GlobalTextField(
                            nombre = "",
                            text = viewModel.newAltura,
                            onValueChange = { viewModel.newAltura = it },
                            trailingIcon = {
                                Text(
                                    "cm",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 14.sp,
                                    color = rojoBench
                                )
                            },
                            textAlign = TextAlign.Center,
                            modifier = Modifier.height(50.dp),
                            backgroundColor = negroOscuroBench,
                            colorText = rojoBench,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }

                    if (showError) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            },
            confirmButton = {
                Text(
                    "Guardar",
                    color = rojoBench,
                    modifier = Modifier
                        .clickable {
                            val error = when {
                                viewModel.editObjetivo -> validarObjetivo(newObjetivo = viewModel.newObjetivo)
                                viewModel.editAltura -> validarAltura(newAltura = viewModel.newAltura)
                                else -> null
                            }

                            if (error != null) {
                                errorMessage = error
                                showError = true
                            } else {
                                showError = false
                                viewModel.guardarCambios()
                            }
                        }
                        .padding(10.dp),
                    fontSize = 15.sp
                )
            },
            dismissButton = {
                Text(
                    "Cancelar",
                    color = Color.White,
                    modifier = Modifier
                        .clickable {
                            viewModel.editObjetivo = false
                            viewModel.editAltura = false
                        }
                        .padding(10.dp),
                    fontSize = 15.sp
                )
            },
            containerColor = negroBench
        )
    }
}

@Composable
fun CambiarPasswordDialog(
    onDismiss: () -> Unit,
    perfilViewModel: PerfilViewModel
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordError = perfilViewModel.passwordError

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cambiar contraseña", color = Color.White) },
        text = {
            Column {
                GlobalTextField(
                    nombre = "Contraseña actual",
                    text = currentPassword,
                    onValueChange = { currentPassword = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colorText = rojoBench,
                    backgroundColor = negroOscuroBench,
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                GlobalTextField(
                    nombre = "Nueva contraseña",
                    text = newPassword,
                    onValueChange = { newPassword = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colorText = rojoBench,
                    backgroundColor = negroOscuroBench,
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                GlobalTextField(
                    nombre = "Confirmar nueva contraseña",
                    text = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colorText = rojoBench,
                    backgroundColor = negroOscuroBench,
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true
                )
                if (passwordError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(passwordError, color = Color.Red)
                }
            }
        },
        confirmButton = {
            Text("Cambiar", color = rojoBench, modifier = Modifier
                .clickable {
                    perfilViewModel.intentarCambiarPassword(currentPassword, newPassword, confirmPassword)
                }
                .padding(10.dp))
        },
        dismissButton = {
            Text("Cancelar", color = Color.White, modifier = Modifier
                .clickable { onDismiss() }
                .padding(10.dp))
        },
        containerColor = negroBench
    )
}
