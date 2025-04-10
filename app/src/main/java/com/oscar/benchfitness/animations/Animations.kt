package com.oscar.benchfitness.animations

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oscar.benchfitness.R
import com.oscar.benchfitness.ui.theme.negroBench
import com.oscar.benchfitness.ui.theme.rojoBench
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fase 1: Aparece suavemente
        alpha.animateTo(1f, animationSpec = tween(1000))

        // Fase 2: Mantener visible
        delay(500)

        // Fase 3: Desvanecerse lentamente
        alpha.animateTo(0f, animationSpec = tween(500))

        // Fase 4: Navegar a la siguiente pantalla
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(negroBench),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_marca),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .alpha(alpha.value)
        )
    }
}
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(negroBench),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando...", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun AnimatedFavoriteStar(isFavorite: Boolean) {
    val transition = updateTransition(targetState = isFavorite, label = "starAnimation")

    // Animación de tamaño
    val size by transition.animateDp(label = "size") { isFav ->
        if (isFav) 26.dp else 24.dp
    }

    // Animación de color
    val tint by transition.animateColor(label = "color") { isFav ->
        if (isFav) rojoBench else Color.Gray
    }

    Image(
        painter = painterResource(
            id = if (isFavorite) R.drawable.estrella_rellena
            else R.drawable.estrella_sinrellena
        ),
        contentDescription = if (isFavorite) "Quitar de favoritos" else "Añadir a favoritos",
        modifier = Modifier.size(size),
        colorFilter = ColorFilter.tint(tint)
    )
}
