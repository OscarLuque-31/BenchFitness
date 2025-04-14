package com.oscar.benchfitness.screens.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oscar.benchfitness.components.FlechitaAtras
import com.oscar.benchfitness.components.GlobalHeader
import com.oscar.benchfitness.models.userData
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.negroClaroBench
import com.oscar.benchfitness.ui.theme.negroOscuroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import com.oscar.benchfitness.utils.interpretarObjetivo
import com.oscar.benchfitness.viewModels.home.GoalViewModel


@Composable
fun GoalScreen(userData: userData, goalViewModel: GoalViewModel, navController: NavController) {

    LaunchedEffect(Unit) {
        goalViewModel.calcularTodasLasCalorias(userData)
    }

    when (userData.objetivo) {
        "Perder peso" -> MainColumn(
            userData = userData,
            objetivo = interpretarObjetivo(userData.objetivo),
            textoExplicativo = goalViewModel.TEXTO_DEFINICION_DF,
            textoComoLograrlo = goalViewModel.TEXTO_COMO_LOGRARLO_DF,
            textoSeguridad = goalViewModel.TEXTO_SEGURIDAD_DF,
            textoRecuerda = goalViewModel.TEXTO_RECUERDA_DF,
            navController = navController,
            viewModel = goalViewModel
        )

        "Mantener peso" -> MainColumn(
            userData = userData,
            objetivo = interpretarObjetivo(userData.objetivo),
            textoExplicativo = goalViewModel.TEXTO_MANTENIMIENTO_MT,
            textoComoLograrlo = goalViewModel.TEXTO_COMO_LOGRARLO_MT,
            textoSeguridad = goalViewModel.TEXTO_SEGURIDAD_MT,
            textoRecuerda = goalViewModel.TEXTO_RECUERDA_MT,
            navController = navController,
            viewModel = goalViewModel
        )

        "Masa muscular" -> MainColumn(
            userData = userData,
            objetivo = interpretarObjetivo(userData.objetivo),
            textoExplicativo = goalViewModel.TEXTO_VOLUMEN_VL,
            textoComoLograrlo = goalViewModel.TEXTO_COMO_LOGRARLO_VL,
            textoSeguridad = goalViewModel.TEXTO_SEGURIDAD_VL,
            textoRecuerda = goalViewModel.TEXTO_RECUERDA_VL,
            navController = navController,
            viewModel = goalViewModel
        )
    }
}

@Composable
fun MainColumn(
    userData: userData,
    objetivo: String,
    textoExplicativo: String,
    textoComoLograrlo: String,
    textoSeguridad: String,
    textoRecuerda: String,
    navController: NavController,
    viewModel: GoalViewModel
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = negroBench)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            GlobalHeader("Bienvenido ${userData.username}")
            ColumnaDatosYCalculos(
                objetivo,
                userData,
                textoExplicativo,
                textoComoLograrlo,
                textoSeguridad,
                textoRecuerda,
                navController,
                viewModel
            )

        }
    }
}

@Composable
fun ColumnaDatosYCalculos(
    objetivo: String,
    userData: userData,
    textoExplicativo: String,
    textoComoLograrlo: String,
    textoSeguridad: String,
    textoRecuerda: String,
    navController: NavController,
    viewModel: GoalViewModel
) {

    val caloriasDefi = viewModel.caloriasDefi.collectAsState()
    val caloriasVol = viewModel.caloriasVol.collectAsState()
    val caloriasMan = viewModel.caloriasMan.collectAsState()
    val caloriasMeta = viewModel.caloriasMeta.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(negroOscuroBench)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            FlechitaAtras(navController)
            Spacer(Modifier.height(20.dp))
            Text(objetivo, fontSize = 29.sp, color = Color.White, fontWeight = FontWeight.Normal)
            when (userData.objetivo) {
                "Perder peso" -> Text(
                    text = "${caloriasDefi.value} kcal",
                    fontSize = 33.sp,
                    color = rojoBench,
                    fontWeight = FontWeight.Bold
                )

                "Mantener peso" -> Text(
                    text = "${caloriasMan.value} kcal",
                    fontSize = 33.sp,
                    color = rojoBench,
                    fontWeight = FontWeight.Bold
                )

                "Masa Muscular" -> Text(
                    text = "${caloriasVol.value} kcal",
                    fontSize = 33.sp,
                    color = rojoBench,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextoInfo("¿Qué es?", textoExplicativo)
            Spacer(modifier = Modifier.height(10.dp))
            TextoInfo("¿Cómo lograrlo?", textoComoLograrlo)
            Spacer(modifier = Modifier.height(10.dp))
            TextoInfo("¿Cuánto déficit es seguro?", textoSeguridad)
            Spacer(modifier = Modifier.height(10.dp))
            TextoInfo("Recuerda:", textoRecuerda)
            ColumnaObjetivos(userData.objetivo,
                caloriasDefi = caloriasDefi.value,
                caloriasMeta = caloriasMeta.value,
                caloriasMan = caloriasMan.value,
                caloriasVol = caloriasVol.value)
        }
    }
}


@Composable
fun TextoInfo(titulo: String, contenido: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = rojoBench)) {
                append("$titulo ")
            }
            withStyle(style = SpanStyle(color = Color.White)) {
                append(contenido)
            }
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        textAlign = TextAlign.Justify
    )
}

@Composable
fun CajaObjetivoCalorias(objetivo: String, calorias: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(negroBench),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(objetivo, color = Color.White, fontWeight = FontWeight.Normal, fontSize = 20.sp)
        Text("$calorias kcal", color = rojoBench)
    }
}

@Composable
fun ColumnaObjetivos(
    objetivo: String,
    caloriasVol: String,
    caloriasDefi: String,
    caloriasMeta: String,
    caloriasMan: String
) {
    when (objetivo) {
        "Perder peso" -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            CajaObjetivoCalorias("Metabolismo", caloriasMeta)
            Spacer(Modifier.height(10.dp))
            CajaObjetivoCalorias("Mantener", caloriasMan)
            Spacer(Modifier.height(10.dp))
            CajaObjetivoCalorias("Superávit", caloriasVol)
        }

        "Mantener peso" -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            CajaObjetivoCalorias("Metabolismo", caloriasMeta)
            Spacer(Modifier.height(10.dp))
            CajaObjetivoCalorias("Déficit", caloriasDefi)
            Spacer(Modifier.height(10.dp))
            CajaObjetivoCalorias("Superávit", caloriasVol)
        }

        "Masa muscular" -> Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            CajaObjetivoCalorias("Metabolismo", caloriasMeta)
            Spacer(Modifier.height(10.dp))
            CajaObjetivoCalorias("Mantener", caloriasMan)
            Spacer(Modifier.height(10.dp))
            CajaObjetivoCalorias("Déficit", caloriasDefi)
        }
    }

}

