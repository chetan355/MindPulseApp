package com.chets.mindpulseapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightSage,
    onPrimary = Color(0xFF20382A),
    primaryContainer = Color(0xFF344D3D),
    onPrimaryContainer = Color(0xFFD5E8D9),

    secondary = LightDustyBlue,
    onSecondary = Color(0xFF26364A),
    secondaryContainer = Color(0xFF3B4B61),
    onSecondaryContainer = Color(0xFFDCE5F1),

    tertiary = LightLavender,
    onTertiary = Color(0xFF352849),
    tertiaryContainer = Color(0xFF4B3D60),
    onTertiaryContainer = Color(0xFFEAE1F5),

    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    outline = Color(0xFF89938B)
)

private val LightColorScheme = lightColorScheme(
    primary = SageGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E8D9),
    onPrimaryContainer = Color(0xFF193526),

    secondary = DustyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE5F1),
    onSecondaryContainer = Color(0xFF26384D),

    tertiary = SoftLavender,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEAE1F5),
    onTertiaryContainer = Color(0xFF302443),

    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    outline = Color(0xFF747D76)
)
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

@Composable
fun MindPulseAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit)
{
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(colorScheme = colorScheme,
        typography = Typography,
        content = content
        )
}


