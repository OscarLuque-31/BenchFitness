package com.oscar.benchfitness.screens.statistics

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.oscar.benchfitness.animations.LoadingScreen
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.components.GlobalTextField
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.ui.theme.verdePrincipiante
import com.oscar.benchfitness.viewModels.statistics.PesoViewModel
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties


@Composable
fun PesoScreen(navController: NavController, viewModel: PesoViewModel) {

    LaunchedEffect(Unit) {
        viewModel.cargarHistorialPesos()
    }


    Column(modifier = Modifier.fillMaxWidth()) {
        GlobalHeader("Peso")
        if (viewModel.isLoading) {
            LoadingScreen()
        } else {
            ColumnaPeso(navController, viewModel)

        }
    }

}

@Composable
fun ColumnaPeso(navController: NavController, viewModel: PesoViewModel) {

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
            Spacer(Modifier.height(20.dp))
            ResumenDiarioPeso(viewModel)
            Spacer(Modifier.height(20.dp))
            FiltroFechasPeso(viewModel)
            Spacer(Modifier.height(20.dp))
            EstadisticaPeso(viewModel)
            Spacer(Modifier.weight(1f))
            BotonAnadirPeso(
                icono = Icons.Default.Add,
                onClick = { viewModel.abrirAgregarPeso() },
                backgroundColor = negroBench,
                modifier = Modifier.size(30.dp)
            )
        }

        // Diálogo para agregar peso
        if (viewModel.mostrarDialogoAgregarPeso) {
            Dialog(
                onDismissRequest = { viewModel.cerrarAgregarPeso() }
            ) {
                AgregarPesoDialog(viewModel)
            }
        }

        if (viewModel.mostrarDialogoConfirmacion) {
            AlertDialog(
                onDismissRequest = {
                    if (!viewModel.isLoading) {
                        viewModel.mostrarDialogoConfirmacion = false
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.confirmarAgregarPeso() },
                        enabled = !viewModel.isLoading
                    ) {
                        Text("Confirmar", color = rojoBench)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.mostrarDialogoConfirmacion = false },
                        enabled = !viewModel.isLoading
                    ) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                title = { Text("¿Confirmar peso?", fontWeight = FontWeight.Bold) },
                text = { Text("¿Quieres añadir este nuevo peso al historial?") },
                containerColor = negroBench,
                titleContentColor = Color.White,
                textContentColor = Color.LightGray
            )
        }


    }
}

@Composable
fun EstadisticaPeso(viewModel: PesoViewModel) {
    val datosPeso = viewModel.datosFiltrados
    if (datosPeso.size < 2) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(rojoBench.copy(alpha = 0.1f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Necesitas al menos 2 mediciones para ver la evolución de tu peso.",
                color = rojoBench,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }


    LineChart(
        modifier = Modifier
            .height(250.dp)
            .padding(horizontal = 10.dp),
        data =
        listOf(
            Line(
                label = "Peso (kg)",
                values = datosPeso,
                color = SolidColor(rojoBench),
                firstGradientFillColor = rojoBench.copy(alpha = .5f),
                secondGradientFillColor = Color.Transparent,
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(width = 2.dp),
            )
        ),
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 300L
        }),
        dividerProperties = DividerProperties(enabled = false),
        indicatorProperties = HorizontalIndicatorProperties(
            textStyle = TextStyle(color = Color.White)
        ),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = TextStyle(color = Color.White) // Esto cambia el color a blanco
        ),
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = TextStyle(color = Color.White) // Esto cambia el color a blanco
        ),
        curvedEdges = true,
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)),
                thickness = 1.dp
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)),
                thickness = 1.dp
            )
        ),
        popupProperties = PopupProperties(
            enabled = true,
            containerColor = Color.DarkGray,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 8.dp,
            contentVerticalPadding = 4.dp,
            textStyle = TextStyle.Default.copy(
                color = Color.White,
                fontSize = 12.sp
            ),
            mode = PopupProperties.Mode.PointMode(threshold = 16.dp)
        ),
        dotsProperties = DotProperties(
            enabled = true,
            radius = 4.dp,
            color = SolidColor(Color.White.copy(alpha = 0.5f)),
            strokeColor = SolidColor(rojoBench.copy(alpha = 0.5f)),
            strokeWidth = 2.dp,
            animationEnabled = true
        )
    )


}


@Composable
fun BotonAnadirPeso(
    icono: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .height(70.dp)
                .width(70.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(
                    backgroundColor
                )
        ) {
            Icon(
                imageVector = icono,
                contentDescription = "add",
                tint = rojoBench,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ResumenDiarioPeso(viewModel: PesoViewModel) {
    val ultimoPeso = viewModel.progreso?.historial?.lastOrNull()?.peso

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(negroBench.copy(alpha = 0.7f))
            .padding(16.dp)
    ) {
        Text(
            text = "RESUMEN DIARIO",
            color = rojoBench,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoResumen(
                titulo = "Peso actual",
                valor = ultimoPeso?.toString() ?: "--",
                unidad = "kg",
                color = Color.White
            )

            InfoResumen(
                titulo = "Cambio",
                valor = viewModel.obtenerDiferenciaPeso(),
                unidad = "",
                color = viewModel.obtenerEstadoPeso()
            )
        }
    }
}

@Composable
fun InfoResumen(
    titulo: String,
    valor: String,
    unidad: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = titulo,
            fontSize = 14.sp,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )
        Text(
            text = "$valor $unidad",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}


@Composable
fun AgregarPesoDialog(viewModel: PesoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(negroBench)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(verdePrincipiante.copy(alpha = 0.1f))
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Consejo",
                tint = verdePrincipiante,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Recuerda pesarte cada semana, el mismo día y preferiblemente en ayunas.",
                color = Color.LightGray,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic
            )
        }

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(negroOscuroBench)
                .padding(horizontal = 10.dp, vertical = 10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    text = viewModel.peso,
                    onValueChange = { newText ->
                        viewModel.pesoInvalido = false
                        if (newText.contains(",")) {
                            viewModel.pesoInvalido = true
                            return@GlobalTextField
                        }
                        val isValid =
                            newText.isEmpty() || newText.matches(Regex("^\\d*(\\.\\d*)?$"))
                        if (isValid) viewModel.peso = newText
                        else viewModel.pesoInvalido = true
                    },
                    trailingIcon = { Text("kg", fontSize = 14.sp, color = rojoBench) },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(120.dp)
                        .height(55.dp),
                    backgroundColor = negroBench,
                    colorText = if (viewModel.pesoInvalido) Color.Red else rojoBench,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )


            }

            if (viewModel.pesoInvalido) {
                Text(
                    text = "Introduce un número válido",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = { viewModel.cerrarAgregarPeso() }
                ) {
                    Text("Cancelar", color = Color.Gray)
                }

                Spacer(Modifier.width(16.dp))

                TextButton(
                    onClick = {
                        if (viewModel.peso.isNotBlank() && !viewModel.pesoInvalido) {
                            viewModel.mostrarDialogoConfirmacion = true
                        } else {
                            viewModel.pesoInvalido = true
                        }
                    }
                ) {
                    Text("Guardar", color = rojoBench)
                }
            }
        }
    }
}

@Composable
fun FiltroFechasPeso(
    viewModel: PesoViewModel
) {
    GlobalDropDownMenu(
        nombreSeleccion = viewModel.filtroSeleccionado,
        opciones = listOf("Última semana", "Último mes", "Último año", "Todo"),
        onValueChange = { viewModel.seleccionarFiltro(it) },
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = negroBench,
        colorItemPulsado = negroOscuroBench.copy(alpha = 0.7f),

        )
}

