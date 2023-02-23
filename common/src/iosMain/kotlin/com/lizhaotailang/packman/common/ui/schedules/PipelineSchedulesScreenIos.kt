package com.lizhaotailang.packman.common.ui.schedules

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.ui.HomeViewModel

@Composable
internal fun PipelineSchedulesScreen(
    innerPaddings: PaddingValues,
    viewModel: HomeViewModel,
    navigateToDetails: (PipelineScheduleListItem) -> Unit
) {
    val pipelineSchedules by viewModel.allPipelineSchedulesFlow.collectAsState()

    PipelineSchedulesScreenContent(
        innerPaddings = innerPaddings,
        schedules = pipelineSchedules.data.orEmpty(),
        navigate = navigateToDetails,
        pipelineSchedules = pipelineSchedules,
        onRefresh = viewModel::getAllPipelineSchedules
    )
}
