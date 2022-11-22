package com.lizhaotailang.packman.android

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// Material 3 color schemes
private val packmanDarkColorScheme = darkColorScheme(
    primary = packmanDarkPrimary,
    onPrimary = packmanDarkOnPrimary,
    primaryContainer = packmanDarkPrimaryContainer,
    onPrimaryContainer = packmanDarkOnPrimaryContainer,
    inversePrimary = packmanDarkPrimaryInverse,
    secondary = packmanDarkSecondary,
    onSecondary = packmanDarkOnSecondary,
    secondaryContainer = packmanDarkSecondaryContainer,
    onSecondaryContainer = packmanDarkOnSecondaryContainer,
    tertiary = packmanDarkTertiary,
    onTertiary = packmanDarkOnTertiary,
    tertiaryContainer = packmanDarkTertiaryContainer,
    onTertiaryContainer = packmanDarkOnTertiaryContainer,
    error = packmanDarkError,
    onError = packmanDarkOnError,
    errorContainer = packmanDarkErrorContainer,
    onErrorContainer = packmanDarkOnErrorContainer,
    background = packmanDarkBackground,
    onBackground = packmanDarkOnBackground,
    surface = packmanDarkSurface,
    onSurface = packmanDarkOnSurface,
    inverseSurface = packmanDarkInverseSurface,
    inverseOnSurface = packmanDarkInverseOnSurface,
    surfaceVariant = packmanDarkSurfaceVariant,
    onSurfaceVariant = packmanDarkOnSurfaceVariant,
    outline = packmanDarkOutline
)

private val packmanLightColorScheme = lightColorScheme(
    primary = packmanLightPrimary,
    onPrimary = packmanLightOnPrimary,
    primaryContainer = packmanLightPrimaryContainer,
    onPrimaryContainer = packmanLightOnPrimaryContainer,
    inversePrimary = packmanLightPrimaryInverse,
    secondary = packmanLightSecondary,
    onSecondary = packmanLightOnSecondary,
    secondaryContainer = packmanLightSecondaryContainer,
    onSecondaryContainer = packmanLightOnSecondaryContainer,
    tertiary = packmanLightTertiary,
    onTertiary = packmanLightOnTertiary,
    tertiaryContainer = packmanLightTertiaryContainer,
    onTertiaryContainer = packmanLightOnTertiaryContainer,
    error = packmanLightError,
    onError = packmanLightOnError,
    errorContainer = packmanLightErrorContainer,
    onErrorContainer = packmanLightOnErrorContainer,
    background = packmanLightBackground,
    onBackground = packmanLightOnBackground,
    surface = packmanLightSurface,
    onSurface = packmanLightOnSurface,
    inverseSurface = packmanLightInverseSurface,
    inverseOnSurface = packmanLightInverseOnSurface,
    surfaceVariant = packmanLightSurfaceVariant,
    onSurfaceVariant = packmanLightOnSurfaceVariant,
    outline = packmanLightOutline
)

@Composable
fun PackmanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !darkTheme

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }

    val wallPaperColorScheme = when {
        dynamicColor &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        darkTheme -> {
            packmanDarkColorScheme
        }
        else -> {
            packmanLightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = wallPaperColorScheme,
        content = content
    )
}
