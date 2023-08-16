package com.lizhaotailang.packman.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.lizhaotailang.packman.common.ui.HomeScreen
import com.lizhaotailang.packman.common.ui.HomeViewModel
import com.lizhaotailang.packman.common.ui.PackmanTheme
import com.lizhaotailang.packman.common.ui.Screen
import com.lizhaotailang.packman.common.ui.barsBackground
import com.lizhaotailang.packman.common.ui.debug.DebugScreen
import com.lizhaotailang.packman.common.ui.job.JobScreen
import com.lizhaotailang.packman.common.ui.schedule.PipelineScheduleScreen
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import kotlin.math.roundToInt

@Composable
internal fun TopPadding(insets: WindowInsets) {
    Box(
        modifier = Modifier
            .windowInsetsTopHeight(insets = insets)
            .fillMaxWidth()
            .background(color = barsBackground())
    )
}

@Composable
internal fun BottomPadding(insets: WindowInsets) {
    Box(
        modifier = Modifier
            .windowInsetsBottomHeight(insets = insets)
            .fillMaxWidth()
            .background(color = barsBackground())
    )
}

@Composable
internal fun MainScreen(insets: WindowInsets) {
    PackmanTheme {
        val currentScreenState = remember { mutableStateOf<Screen>(Screen.HomeScreen) }

        val homeViewModel = remember(key1 = HomeViewModel.KEY) { HomeViewModel() }

        when (val screen = currentScreenState.value) {
            Screen.DebugScreen -> {
                DebugScreen(
                    insets = insets,
                    currentScreenState = currentScreenState
                )
            }
            Screen.HomeScreen -> {
                HomeScreen(
                    insets = insets,
                    currentScreenState = currentScreenState,
                    viewModel = homeViewModel
                )
            }
            is Screen.JobScreen -> {
                JobScreen(
                    insets = insets,
                    jobId = screen.jobId,
                    cancelable = screen.cancelable,
                    retryable = screen.retryable,
                    triggered = screen.triggered,
                    currentScreenState = currentScreenState
                )
            }
            is Screen.ScheduleScreen -> {
                PipelineScheduleScreen(
                    insets = insets,
                    pipelineScheduleId = screen.pipelineScheduleId,
                    currentScreenState = currentScreenState
                )
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("UNUSED")
fun mainViewController(window: UIWindow): UIViewController {
    val insets = window.safeAreaInsets.useContents {
        WindowInsets(
            left.roundToInt(),
            (top + bottom).roundToInt(),
            right.roundToInt(),
            bottom.roundToInt()
        )
    }

    val mainController = ComposeUIViewController {
        MainScreen(insets = insets)
    }

    return mainController
}
