package com.lizhaotailang.packman.common.ui

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lizhaotailang.packman.common.R
import com.lizhaotailang.packman.common.ui.components.PackmanTopBar
import com.lizhaotailang.packman.common.ui.debug.DebugScreen
import com.lizhaotailang.packman.common.ui.job.JobScreen
import com.lizhaotailang.packman.common.ui.jobs.JobsScreen
import com.lizhaotailang.packman.common.ui.new.NewJobScreen
import com.lizhaotailang.packman.common.ui.schedule.PipelineScheduleScreen
import com.lizhaotailang.packman.common.ui.schedules.PipelineSchedulesScreen

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen() {
    val selectedItemState = remember { mutableStateOf(MainScreenNavigationItem.Jobs) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val navController = LocalNavController.current

    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel(app = context.applicationContext as Application)
        }
    )

    val showSnackbarMessage = viewModel.snackbarMessage.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            PackmanTopBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                            onLongClick = {
                                navController.navigate(route = Screen.DebugScreen.route)
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
        content = { innerPadding ->
            when (selectedItemState.value) {
                MainScreenNavigationItem.Jobs -> {
                    JobsScreen(innerPaddings = innerPadding)
                }
                MainScreenNavigationItem.New -> {
                    NewJobScreen(innerPaddings = innerPadding)
                }
                MainScreenNavigationItem.Schedules -> {
                    PipelineSchedulesScreen(innerPaddings = innerPadding)
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
}

@Composable
fun MainNavHost(startDestination: Screen) {
    val currentRoute = remember { mutableStateOf(startDestination.route) }

    NavHost(
        navController = LocalNavController.current,
        startDestination = startDestination.route
    ) {
        composable(route = Screen.HomeScreen.route) {
            currentRoute.value = Screen.HomeScreen.route
            HomeScreen()
        }
        composable(route = Screen.DebugScreen.route) {
            currentRoute.value = Screen.DebugScreen.route
            DebugScreen()
        }
        composable(
            route = Screen.JobScreen.ROUTE,
            arguments = listOf(
                navArgument(name = Screen.ARG_JOB_ID) {
                    type = NavType.StringType
                },
                navArgument(name = Screen.ARG_CANCELABLE) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(name = Screen.ARG_RETRYABLE) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(name = Screen.ARG_TRIGGERED) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            currentRoute.value = Screen.JobScreen.ROUTE
            JobScreen(
                jobId = backStackEntry.arguments?.getString(Screen.ARG_JOB_ID)
                    ?: return@composable,
                cancelable = backStackEntry.arguments?.getBoolean(Screen.ARG_CANCELABLE)
                    ?: false,
                retryable = backStackEntry.arguments?.getBoolean(Screen.ARG_RETRYABLE)
                    ?: false,
                triggered = backStackEntry.arguments?.getBoolean(Screen.ARG_TRIGGERED)
                    ?: false
            )
        }
        composable(
            route = Screen.ScheduleScreen.ROUTE,
            arguments = listOf(
                navArgument(name = Screen.ARG_PIPELINE_SCHEDULE_ID) {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            currentRoute.value = Screen.ScheduleScreen.ROUTE

            PipelineScheduleScreen(
                pipelineScheduleId = backStackEntry.arguments?.getInt(Screen.ARG_PIPELINE_SCHEDULE_ID)
                    ?: return@composable
            )
        }
    }
}
