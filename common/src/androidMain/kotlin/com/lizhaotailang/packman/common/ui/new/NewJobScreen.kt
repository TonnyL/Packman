package com.lizhaotailang.packman.common.ui.new

import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.common.ui.HomeViewModel

@Composable
fun NewJobScreen(innerPaddings: PaddingValues) {
    val context = LocalContext.current

    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel(app = context.applicationContext as Application)
        }
    )

    val requestState = viewModel.requestFlow.collectAsStateWithLifecycle()

    val histories by viewModel.database.historiesFlow.collectAsStateWithLifecycle()

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
