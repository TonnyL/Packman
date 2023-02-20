package com.lizhaotailang.packman.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import platform.UIKit.UIViewController

@Suppress("UNUSED")
@OptIn(ExperimentalMaterial3Api::class)
fun mainViewController(): UIViewController {
    return Application("TravelApp-KMP") {
        MaterialTheme {
            Column {
                // To skip upper part of screen.
                Box(
                    modifier = Modifier
                        .height(40.dp)
                )
                Text("Packman")
            }
        }
    }
}
