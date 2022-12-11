package com.gear.add_remove_location.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.White,
    primaryVariant = Gray500,
    secondary = Primary500,
    background = Color.Black,
    surface = Color.Black
)

private val LightColorPalette = lightColors(
    primary = Color.Black,
    primaryVariant = Gray500,
    secondary = Primary500,
    background = Color.White,
    surface = Color.White
)

@Composable
fun LocationWeatheryTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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