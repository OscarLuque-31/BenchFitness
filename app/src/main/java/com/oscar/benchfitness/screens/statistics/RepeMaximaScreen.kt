package com.oscar.benchfitness.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalButton
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.components.InfoDialog
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.statistics.CalculosViewModel

@Composable
fun RepeMaximaScreen(navController: NavController, viewModel: CalculosViewModel) {

    Column(modifier = Modifier.fillMaxWidth()) {
        GlobalHeader("Repetición Máxima")
        ColumnaRepeMaxima(navController, viewModel)
    }

}

@Composable
fun ColumnaRepeMaxima(navController: NavController, viewModel: CalculosViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            FlechitaAtras(navController = navController)

            TituloYDefinicion()

            ColumnaPesoyRepes(viewModel)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                GlobalButton(
                    "Calcular",
                    modifier = Modifier.height(50.dp),
                    colorText = if (viewModel.esFormularioRepeMaxCompletado()) Color.White else Color.Gray,
                    backgroundColor = if (viewModel.esFormularioRepeMaxCompletado()) negroBench else Color.DarkGray
                ) {
                    when {
                        !viewModel.esPesoEjercicioValido() -> {
                            viewModel.mensajeError =
                                "Peso inválido. Ingresa una peso entre 10 y 300 kilos."
                            viewModel.showDialog = true
                        }

                        !viewModel.sonRepeticionesValidas() -> {
                            viewModel.mensajeError =
                                "Repeticiones inválidas. Ingresa repeticiones entre 1 y 15"
                            viewModel.showDialog = true
                        }

                        viewModel.esFormularioRepeMaxValido() -> {
                            viewModel.calcularRepeMaxima()
                        }

                        else -> {
                            viewModel.mensajeError = "Completa todos los campos correctamente."
                            viewModel.showDialog = true
                        }
                    }
                }
            }
            // Verificar si ya se calculó el RM
            if (viewModel.isCalculated) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            "Este es tu RM",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Light,
                            color = rojoBench
                        )
                        Text(
                            "${viewModel.rm} kg",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = rojoBench
                        )
                    }
                }
            }

            // Texto de advertencia al final
            if (!viewModel.isCalculated) {
                Spacer(modifier = Modifier.weight(1f))  // Asegura que el texto quede al fondo
            }
            Text(
                "Ten en cuenta que el 1RM real puede variar según la técnica y el estado físico en el momento de realizar la prueba.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }
    }


    InfoDialog(
        title = "Datos no válidos",
        showDialog = viewModel.showDialog,
        onDismiss = { viewModel.showDialog = false }
    ) {
        Text(viewModel.mensajeError, color = rojoBench)
    }
}

@Composable
fun TituloYDefinicion() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculadora de RM",
            fontSize = 25.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "El 1RM (Repetición Máxima) es el peso máximo que puedes levantar en un solo intento, " +
                    "basado en la cantidad de peso que puedes levantar durante varias repeticiones.",
            fontSize = 12.sp,
            color = rojoBench,
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun ColumnaPesoyRepes(viewModel: CalculosViewModel) {
    Column(
        modifier = Modifier.padding(vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(negroBench)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                "Peso",
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            GlobalTextField(
                nombre = "",
                text = viewModel.pesoEjercicio,
                onValueChange = { viewModel.pesoEjercicio = it },
                trailingIcon = { Text("kg", fontSize = 14.sp, color = rojoBench) },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(120.dp)
                    .height(55.dp),
                backgroundColor = negroOscuroBench,
                colorText = rojoBench,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(negroBench)
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                "Repeticiones",
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            GlobalTextField(
                nombre = "",
                text = viewModel.repeticiones,
                onValueChange = { viewModel.repeticiones = it },
                trailingIcon = { Text("reps", fontSize = 14.sp, color = rojoBench) },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(120.dp)
                    .height(55.dp),
                backgroundColor = negroOscuroBench,
                colorText = rojoBench,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
