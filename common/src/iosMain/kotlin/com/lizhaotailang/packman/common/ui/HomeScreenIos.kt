package com.lizhaotailang.packman.common.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lizhaotailang.packman.common.BottomPadding
import com.lizhaotailang.packman.common.TopPadding
import com.lizhaotailang.packman.common.ui.components.PackmanTopBar
import com.lizhaotailang.packman.common.ui.jobs.JobsScreen
import com.lizhaotailang.packman.common.ui.new.NewJobScreen
import com.lizhaotailang.packman.common.ui.schedules.PipelineSchedulesScreen

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
internal fun HomeScreen(
    insets: WindowInsets,
    currentScreenState: MutableState<Screen>,
    viewModel: HomeViewModel
) {
    PackmanTheme {
        val selectedItemState =
            remember { mutableStateOf(MainScreenNavigationItem.Jobs) }
        val snackbarHostState = remember { SnackbarHostState() }
        val showSnackbarMessage = viewModel.snackbarMessage.collectAsState()

        Column {
            TopPadding(insets = insets)

            Scaffold(
                modifier = Modifier.weight(weight = 1f),
                topBar = {
                    PackmanTopBar(
                        title = {
                            Text(
                                text = "Packman",
                                modifier = Modifier.combinedClickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { },
                                    onLongClick = {
                                        currentScreenState.value = Screen.DebugScreen
                                    }
                                )
                            )
                        }
                    )
                },
                bottomBar = {
                    HomeBottomBar(selectedItemState = selectedItemState)
                },
                snackbarHost = { SnackbarHost(snackbarHostState) },
                content = { innerPaddings ->
                    when (selectedItemState.value) {
                        MainScreenNavigationItem.Jobs -> {
                            JobsScreen(
                                innerPaddings = innerPaddings,
                                viewModel = viewModel,
                                navigateToJobDetails = { job ->
                                    job.id?.let {
                                        currentScreenState.value = Screen.JobScreen(
                                            jobId = it.replace("#", ""),
                                            cancelable = job.cancelable,
                                            retryable = job.retryable,
                                            triggered = job.triggered == true
                                        )
                                    }
                                }
                            )
                        }
                        MainScreenNavigationItem.New -> {
                            NewJobScreen(
                                innerPaddings = innerPaddings,
                                viewModel = viewModel
                            )
                        }
                        MainScreenNavigationItem.Schedules -> {
                            PipelineSchedulesScreen(
                                innerPaddings = innerPaddings,
                                viewModel = viewModel,
                                navigateToDetails = { schedule ->
                                    currentScreenState.value = Screen.ScheduleScreen(
                                        pipelineScheduleId = schedule.id
                                    )
                                }
                            )
                        }
                    }

                    val snackbarMessage = showSnackbarMessage.value
                    LaunchedEffect(snackbarMessage) {
                        if (snackbarMessage.isNotEmpty()) {
                            val result = snackbarHostState.showSnackbar(
                                message = snackbarMessage,
                                duration = SnackbarDuration.Long,
                                withDismissAction = true
                            )
                            when (result) {
                                SnackbarResult.Dismissed -> {
                                    viewModel.showSnackbarMessage("")
                                }
                                SnackbarResult.ActionPerformed -> {
                                }
                            }
                        }
                    }
                }
            )

            BottomPadding(insets = insets)
        }
    }
}
