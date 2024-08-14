package com.example.assignment2.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Color(0xFFEE0067),
    primaryContainer = Color(0xFFA700B3),
    secondary = Color(0xFFDA03C4),
    onPrimary = Color.White,
    onSecondary = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFC86D5),
    primaryContainer = Color(0xFFA100B3),
    secondary = Color(0xFFDA03D6),
    onPrimary = Color.Black,
    onSecondary = Color.White
)

@Composable
fun Assignment2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
