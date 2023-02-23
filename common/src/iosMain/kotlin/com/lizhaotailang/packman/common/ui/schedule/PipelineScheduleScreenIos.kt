package com.lizhaotailang.packman.common.ui.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lizhaotailang.packman.common.BottomPadding
import com.lizhaotailang.packman.common.TopPadding
import com.lizhaotailang.packman.common.ui.Screen

@Composable
internal fun PipelineScheduleScreen(
    insets: WindowInsets,
    pipelineScheduleId: Int,
    currentScreenState: MutableState<Screen>
) {
    val viewModel = remember { PipelineScheduleViewModel(pipelineScheduleId = pipelineScheduleId) }

    val pipelineScheduleResource by viewModel.getASinglePipelineScheduleFlow.collectAsState()

    Column {
        TopPadding(insets = insets)

        Box(modifier = Modifier.weight(weight = 1f)) {
            PipelineScheduleScreenContent(
                pipelineScheduleId = pipelineScheduleId,
                pipelineScheduleResource = pipelineScheduleResource,
                retry = viewModel::getASinglePipelineSchedule,
                navigateUp = {
                    currentScreenState.value = Screen.HomeScreen
                }
            )
        }

        BottomPadding(insets = insets)
    }
}
