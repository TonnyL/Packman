package com.lizhaotailang.packman.common.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
internal fun PackmanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val wallPaperColorScheme = if (darkTheme) {
        packmanDarkColorScheme
    } else {
        packmanLightColorScheme
    }

    MaterialTheme(
        colorScheme = wallPaperColorScheme,
        content = content
    )
}
