package com.lizhaotailang.packman.common.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.common.ui.LocalNavController

@Composable
fun PipelineScheduleScreen(pipelineScheduleId: Int) {
    val navController = LocalNavController.current
    val viewModel = viewModel(
        initializer = {
            PipelineScheduleViewModel(pipelineScheduleId = pipelineScheduleId)
        }
    )
    val pipelineScheduleResource by viewModel.getASinglePipelineScheduleFlow.collectAsStateWithLifecycle()

    PipelineScheduleScreenContent(
        pipelineScheduleId = pipelineScheduleId,
        pipelineScheduleResource = pipelineScheduleResource,
        retry = viewModel::getASinglePipelineSchedule,
        navigateUp = {
            navController.navigateUp()
        }
    )
}
