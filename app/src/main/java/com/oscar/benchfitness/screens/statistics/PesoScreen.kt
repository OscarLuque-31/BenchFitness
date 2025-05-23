package com.oscar.benchfitness.screens.statistics

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
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
        ColumnaPeso(navController, viewModel)
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
            EstadisticaPeso(viewModel)
        }


    }
}

@Composable
fun EstadisticaPeso(viewModel: PesoViewModel) {

    val datosPeso = remember(viewModel.progreso) {
        viewModel.progreso?.historial
            ?.sortedBy { it.fecha }
            ?.map { it.peso }
    }

    if (datosPeso != null) {
        LineChart(
            modifier = Modifier
                .height(200.dp)
                .padding(horizontal = 22.dp),
            data = remember {
                listOf(
                    Line(
                        label = "Peso",
                        values = datosPeso,
                        color = SolidColor(rojoBench),
                        firstGradientFillColor = rojoBench.copy(alpha = .5f),
                        secondGradientFillColor = Color.Transparent,
                        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                        gradientAnimationDelay = 1000,
                        drawStyle = DrawStyle.Stroke(width = 2.dp),
                    )
                )
            },
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
}