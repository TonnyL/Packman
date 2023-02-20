package com.lizhaotailang.packman.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val packmanLightPrimary = Color(0xFF825500)
val packmanLightOnPrimary = Color(0xFFFFFFFF)
val packmanLightPrimaryContainer = Color(0xFFFFDDAE)
val packmanLightOnPrimaryContainer = Color(0xFF2A1800)
val packmanLightSecondary = Color(0xFF6F5B40)
val packmanLightOnSecondary = Color(0xFFFFFFFF)
val packmanLightSecondaryContainer = Color(0xFFFADEBC)
val packmanLightOnSecondaryContainer = Color(0xFF271904)
val packmanLightTertiary = Color(0xFF516440)
val packmanLightOnTertiary = Color(0xFFFFFFFF)
val packmanLightTertiaryContainer = Color(0xFFD3EABC)
val packmanLightOnTertiaryContainer = Color(0xFF102004)
val packmanLightError = Color(0xFFBA1B1B)
val packmanLightErrorContainer = Color(0xFFFFDAD4)
val packmanLightOnError = Color(0xFFFFFFFF)
val packmanLightOnErrorContainer = Color(0xFF410001)
val packmanLightBackground = Color(0xFFFCFCFC)
val packmanLightOnBackground = Color(0xFF1F1B16)
val packmanLightSurface = Color(0xFFFCFCFC)
val packmanLightOnSurface = Color(0xFF1F1B16)
val packmanLightSurfaceVariant = Color(0xFFF0E0CF)
val packmanLightOnSurfaceVariant = Color(0xFF4F4539)
val packmanLightOutline = Color(0xFF817567)
val packmanLightInverseOnSurface = Color(0xFFF9EFE6)
val packmanLightInverseSurface = Color(0xFF34302A)
val packmanLightPrimaryInverse = Color(0xFFFFB945)

val packmanDarkPrimary = Color(0xFFFFB945)
val packmanDarkOnPrimary = Color(0xFF452B00)
val packmanDarkPrimaryContainer = Color(0xFF624000)
val packmanDarkOnPrimaryContainer = Color(0xFFFFDDAE)
val packmanDarkSecondary = Color(0xFFDDC3A2)
val packmanDarkOnSecondary = Color(0xFF3E2E16)
val packmanDarkSecondaryContainer = Color(0xFF56442B)
val packmanDarkOnSecondaryContainer = Color(0xFFFADEBC)
val packmanDarkTertiary = Color(0xFFB8CEA2)
val packmanDarkOnTertiary = Color(0xFF243516)
val packmanDarkTertiaryContainer = Color(0xFF3A4C2B)
val packmanDarkOnTertiaryContainer = Color(0xFFD3EABC)
val packmanDarkError = Color(0xFFFFB4A9)
val packmanDarkErrorContainer = Color(0xFF930006)
val packmanDarkOnError = Color(0xFF680003)
val packmanDarkOnErrorContainer = Color(0xFFFFDAD4)
val packmanDarkBackground = Color(0xFF1F1B16)
val packmanDarkOnBackground = Color(0xFFEAE1D9)
val packmanDarkSurface = Color(0xFF1F1B16)
val packmanDarkOnSurface = Color(0xFFEAE1D9)
val packmanDarkSurfaceVariant = Color(0xFF4F4539)
val packmanDarkOnSurfaceVariant = Color(0xFFD3C4B4)
val packmanDarkOutline = Color(0xFF9C8F80)
val packmanDarkInverseOnSurface = Color(0xFF32281A)
val packmanDarkInverseSurface = Color(0xFFEAE1D9)
val packmanDarkPrimaryInverse = Color(0xFF624000)

@Composable
internal fun barsBackground() = MaterialTheme.colorScheme.surfaceColorAtElevation(
    elevation = NavigationBarDefaults.Elevation
).copy(alpha = .97f)
