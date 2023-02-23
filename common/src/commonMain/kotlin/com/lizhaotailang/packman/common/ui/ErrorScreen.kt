package com.lizhaotailang.packman.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun ErrorScreen(action: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 64.dp)
    ) {
        Image(
            painter = ErrorScreenImage(),
            contentDescription = null,
            modifier = Modifier
                .size(size = 200.dp)
                .clickable(
                    onClick = action,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )
        Spacer(modifier = Modifier.height(height = 16.dp))
        TextButton(onClick = action) {
            Text(
                text = "Something went wrong, tap to reload...",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
internal expect fun ErrorScreenImage(): Painter
