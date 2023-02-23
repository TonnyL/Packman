package com.lizhaotailang.packman.common.ui.schedules

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.common.ui.HomeViewModel
import com.lizhaotailang.packman.common.ui.LocalNavController
import com.lizhaotailang.packman.common.ui.Screen

@Composable
fun PipelineSchedulesScreen(innerPaddings: PaddingValues) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel(app = context.applicationContext as Application)
        }
    )

    val pipelineSchedules by viewModel.allPipelineSchedulesFlow.collectAsStateWithLifecycle()

    PipelineSchedulesScreenContent(
        innerPaddings = innerPaddings,
        schedules = pipelineSchedules.data.orEmpty(),
        navigate = { item ->
            navController.navigate(
                route = Screen.ScheduleScreen.ROUTE
                    .replace(
                        "{${Screen.ARG_PIPELINE_SCHEDULE_ID}}",
                        item.id.toString()
                    )
            )
        },
        pipelineSchedules = pipelineSchedules,
        onRefresh = viewModel::getAllPipelineSchedules
    )
}
