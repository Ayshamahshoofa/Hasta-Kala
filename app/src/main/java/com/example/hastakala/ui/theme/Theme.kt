package com.example.hastakala.ui.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BrandBrown,
    secondary = ArtisanTan,
    tertiary = BrandRed,
    background = ArtisanSand,
    surface = ArtisanSand
)

@Composable
fun HastaKalaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
