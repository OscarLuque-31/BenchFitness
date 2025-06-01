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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.animations.LoadingScreen
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalDropDownMenu
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.models.statistics.ExerciseProgress
import com.oscar.benchfitness.models.statistics.StatisticsExercise
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.viewModels.statistics.ProgresoViewModel
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
import ir.ehsannarmani.compose_charts.models.StrokeStyle


@Composable
fun ProgresoScreen(navController: NavController, viewModel: ProgresoViewModel) {

    LaunchedEffect(Unit) {
        viewModel.cargarProgreso()
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        GlobalHeader("Progreso")
        if (viewModel.isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                ColumnaProgreso(navController, viewModel)

            }
        }

    }
}

@Composable
fun ColumnaProgreso(navController: NavController, viewModel: ProgresoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
            ResumenDiarioProgreso(viewModel)
            Spacer(Modifier.height(20.dp))
            ElegirEjercicioProgreso(viewModel)
            Spacer(Modifier.height(20.dp))
            FiltroFechasProgreso(viewModel)
            Spacer(Modifier.height(20.dp))
            EstadisticaProgreso(progress = viewModel.ejercicioFiltrado)
        }
    }
}


@Composable
fun EstadisticaProgreso(progress: ExerciseProgress) {
    val historial = progress.historial

    if (historial.size < 2) {
        MensajeInsuficienteDatos()
        return
    }

    val todasFechas = historial.map { it.fecha }
    val intervalo = maxOf(1, todasFechas.size / 3)
    val fechasReducidas = todasFechas.mapIndexed { index, fecha ->
        if (index % intervalo == 0) fecha else ""
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        PesoMaximoChart(historial, fechasReducidas)
        Spacer(modifier = Modifier.height(40.dp))
        RepsPorSerieChart(historial, fechasReducidas)
        Spacer(modifier = Modifier.height(40.dp))
        KilosPorSerieChart(historial, fechasReducidas)
    }
}

@Composable
fun MensajeInsuficienteDatos() {
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
            text = "Necesitas al menos 2 mediciones para ver la evolución del ejercicio.",
            color = rojoBench,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun PesoMaximoChart(historial: List<StatisticsExercise>, fechas: List<String>) {
    val pesosMaximos = historial.map { it.series.maxOfOrNull { s -> s.peso } ?: 0.0 }

    val linea = Line(
        label = "Peso (kg)",
        values = pesosMaximos,
        color = SolidColor(rojoBench),
        firstGradientFillColor = rojoBench.copy(alpha = .5f),
        secondGradientFillColor = Color.Transparent,
        strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
        gradientAnimationDelay = 1000,
        drawStyle = DrawStyle.Stroke(width = 2.dp),
        curvedEdges = true,
        dotProperties = DotProperties(
            enabled = true,
            radius = 4.dp,
            color = SolidColor(Color.White),
            strokeColor = SolidColor(rojoBench),
            strokeWidth = 2.dp
        )
    )

    Text(
        text = "Peso máximo por sesión",
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    LineChart(
        modifier = Modifier.height(280.dp),
        data = listOf(linea),
        animationMode = AnimationMode.Together(delayBuilder = { it * 300L }),
        curvedEdges = true,
        dividerProperties = DividerProperties(enabled = false),
        indicatorProperties = HorizontalIndicatorProperties(textStyle = TextStyle(color = Color.White)),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = TextStyle(color = Color.White)
        ),
        labelProperties = LabelProperties(
            enabled = true,
            labels = fechas,
            textStyle = TextStyle(color = Color.White)
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)), thickness = 1.dp
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)), thickness = 1.dp
            )
        ),
        popupProperties = PopupProperties(
            enabled = true,
            containerColor = Color.DarkGray,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 8.dp,
            contentVerticalPadding = 4.dp,
            textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
            mode = PopupProperties.Mode.PointMode(threshold = 16.dp)
        ),
        dotsProperties = linea.dotProperties!!
    )
}

@Composable
fun RepsPorSerieChart(historial: List<StatisticsExercise>, fechas: List<String>) {
    val maxSeries = historial.maxOf { it.series.size }

    val lineas = (0 until maxSeries).map { index ->
        val values = historial.map { it.series.getOrNull(index)?.reps?.toDouble() ?: 0.0 }
        val color = Color.hsv((index * 60f) % 360, 0.7f, 0.9f)

        Line(
            label = "Serie ${index + 1}",
            values = values,
            color = SolidColor(color),
            firstGradientFillColor = Color.Transparent,
            secondGradientFillColor = Color.Transparent,
            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
            gradientAnimationDelay = (500 + index * 150).toLong(),
            drawStyle = DrawStyle.Stroke(
                width = 3.dp,
                strokeStyle = StrokeStyle.Dashed(
                    intervals = floatArrayOf(10f, 10f),
                    phase = 15f
                )
            ),
            curvedEdges = true,
            dotProperties = DotProperties(
                enabled = true,
                radius = 4.dp,
                color = SolidColor(Color.White),
                strokeColor = SolidColor(color),
                strokeWidth = 2.dp
            )
        )
    }

    Text(
        text = "Repeticiones por serie",
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    LineChart(
        modifier = Modifier.height(280.dp),
        data = lineas,
        animationMode = AnimationMode.Together(delayBuilder = { it * 300L }),
        curvedEdges = true,
        dividerProperties = DividerProperties(enabled = false),
        indicatorProperties = HorizontalIndicatorProperties(textStyle = TextStyle(color = Color.White)),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = TextStyle(color = Color.White)
        ),
        labelProperties = LabelProperties(
            enabled = true,
            labels = fechas,
            textStyle = TextStyle(color = Color.White)
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)), thickness = 1.dp
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)), thickness = 1.dp
            )
        ),
        popupProperties = PopupProperties(
            enabled = true,
            containerColor = Color.DarkGray,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 8.dp,
            contentVerticalPadding = 4.dp,
            textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
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
fun KilosPorSerieChart(historial: List<StatisticsExercise>, fechas: List<String>) {
    val maxSeries = historial.maxOf { it.series.size }

    val lineas = (0 until maxSeries).map { index ->
        val values = historial.map { it.series.getOrNull(index)?.peso ?: 0.0 }
        val color = Color.hsv((index * 60f + 30f) % 360, 0.8f, 0.9f)

        Line(
            label = "Serie ${index + 1}",
            values = values,
            color = SolidColor(color),
            firstGradientFillColor = Color.Transparent,
            secondGradientFillColor = Color.Transparent,
            strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
            gradientAnimationDelay = (500 + index * 150).toLong(),
            drawStyle = DrawStyle.Stroke(
                width = 3.dp,
                strokeStyle = StrokeStyle.Dashed(
                    intervals = floatArrayOf(10f, 10f),
                    phase = 15f
                )
            ),
            curvedEdges = true,
            dotProperties = DotProperties(
                enabled = true,
                radius = 4.dp,
                color = SolidColor(Color.White),
                strokeColor = SolidColor(color),
                strokeWidth = 2.dp
            )
        )
    }

    Text(
        text = "Kilos por serie",
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    LineChart(
        modifier = Modifier.height(280.dp),
        data = lineas,
        animationMode = AnimationMode.Together(delayBuilder = { it * 300L }),
        curvedEdges = true,
        dividerProperties = DividerProperties(enabled = false),
        indicatorProperties = HorizontalIndicatorProperties(textStyle = TextStyle(color = Color.White)),
        labelHelperProperties = LabelHelperProperties(
            enabled = true,
            textStyle = TextStyle(color = Color.White)
        ),
        labelProperties = LabelProperties(
            enabled = true,
            labels = fechas,
            textStyle = TextStyle(color = Color.White)
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)), thickness = 1.dp
            ),
            yAxisProperties = GridProperties.AxisProperties(
                color = SolidColor(rojoBench.copy(alpha = 0.5f)), thickness = 1.dp
            )
        ),
        popupProperties = PopupProperties(
            enabled = true,
            containerColor = Color.DarkGray,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 8.dp,
            contentVerticalPadding = 4.dp,
            textStyle = TextStyle(color = Color.White, fontSize = 12.sp),
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
fun FiltroFechasProgreso(viewModel: ProgresoViewModel) {
    GlobalDropDownMenu(
        nombreSeleccion = viewModel.filtroSeleccionado,
        opciones = listOf("Última semana", "Último mes", "Último año", "Todo"),
        onValueChange = { viewModel.seleccionarFiltro(it) },
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = negroBench,
        colorItemPulsado = negroOscuroBench.copy(alpha = 0.7f)
    )
}

@Composable
fun ResumenDiarioProgreso(viewModel: ProgresoViewModel) {
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Ejercicio más trabajado centrado
            InfoResumen(
                titulo = "Más trabajado",
                valor = viewModel.ejercicioTopHoy ?: "--",
                unidad = "",
                color = Color.Yellow
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Columna izquierda
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoResumen(
                    "Ejercicios",
                    viewModel.totalEjerciciosHoy.toString(),
                    "",
                    Color.White
                )
                InfoResumen("Repeticiones", viewModel.totalRepsHoy.toString(), "", Color.White)
            }

            // Columna derecha
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfoResumen("Series", viewModel.totalSeriesHoy.toString(), "", Color.White)
                InfoResumen("Máx. peso", viewModel.maxPesoHoy.toString(), "kg", Color.Green)
            }
        }
    }
}


@Composable
fun ElegirEjercicioProgreso(viewModel: ProgresoViewModel) {
    val ejercicios = viewModel.progresoTotal

    if (ejercicios.isEmpty()) return

    GlobalDropDownMenu(
        selectedItem = viewModel.ejercicioSeleccionado,
        opciones = ejercicios,
        onValueChange = { viewModel.seleccionarEjercicio(it) },
        itemText = { it.nombre },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = rojoBench,
        colorText = negroOscuroBench,
        colorItemPulsado = Color.White.copy(alpha = 0.7f),
        colorFlechita = negroOscuroBench
    )
}