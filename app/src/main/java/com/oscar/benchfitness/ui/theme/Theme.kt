package com.oscar.benchfitness.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MainColorScheme = lightColorScheme(
    /* Puedes definir colores adicionales si es necesario
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun BenchfitnessTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MainColorScheme,
        typography = Typography,
        content = content
    )
}
