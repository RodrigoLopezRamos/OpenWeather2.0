package com.globant.codechanllenge.openweather.ui.theme;

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = WeatherGreen,
    primaryVariant = Grey200,
    secondary = Teal200,
    onSurface = colorSnow,
    surface = Black200,
    background = Black100
)

private val LightColorPalette = lightColors(
    primary = WeatherGreen,
    primaryVariant = Grey200,
    secondary = Teal200,
    onSurface = Black200,
    surface = colorSnow,
    background = white
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}