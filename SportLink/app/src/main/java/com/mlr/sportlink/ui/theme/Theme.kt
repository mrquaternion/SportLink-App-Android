package com.mlr.sportlink.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SportLinkPrimary,
    onPrimary = Color.White,
    primaryContainer = SportLinkSecondary,
    onPrimaryContainer = Color.White,
    secondary = SportLinkSecondary,
    onSecondary = Color.White,
    tertiary = SportLinkTertiary,
    onTertiary = SportLinkPrimary,
    background = SportLinkBackgroundDark,
    onBackground = SportLinkOnDark,
    surface = SportLinkComponentDark,
    onSurface = SportLinkOnDark,
    surfaceVariant = SportLinkComponentDark,
    onSurfaceVariant = SportLinkOnDark.copy(alpha = 0.78f),
    outline = SportLinkOutlineDark,
    outlineVariant = SportLinkOutlineDark,
)

private val LightColorScheme = lightColorScheme(
    primary = SportLinkPrimary,
    onPrimary = Color.White,
    primaryContainer = SportLinkTertiary,
    onPrimaryContainer = SportLinkPrimary,
    secondary = SportLinkSecondary,
    onSecondary = Color.White,
    tertiary = SportLinkTertiary,
    onTertiary = SportLinkPrimary,
    background = SportLinkBackgroundLight,
    onBackground = SportLinkOnLight,
    surface = SportLinkComponentLight,
    onSurface = SportLinkOnLight,
    surfaceVariant = SportLinkBackgroundSecondLight,
    onSurfaceVariant = SportLinkOnLight.copy(alpha = 0.72f),
    outline = SportLinkOutlineLight,
    outlineVariant = SportLinkOutlineLight,
)

@Composable
fun SportLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
