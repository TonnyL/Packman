package com.lizhaotailang.packman.common.ui.new

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lizhaotailang.packman.common.ui.HomeViewModel

@Composable
internal fun NewJobScreen(
    innerPaddings: PaddingValues,
    viewModel: HomeViewModel
) {
    val histories by viewModel.database.historiesFlow.collectAsState()

    val requestState = viewModel.requestFlow.collectAsState()

    NewJobScreenContent(
        innerPaddings = innerPaddings,
        requestState = requestState,
        histories = histories,
        onRunClick = { branch, variants ->
            viewModel.triggerNewPipeline(
                branch = branch,
                variants = variants
            )
        },
        deleteHistory = {
            viewModel.deleteHistory(it)
        }
    )

}
