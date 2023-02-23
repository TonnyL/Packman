package com.lizhaotailang.packman.common.ui.debug

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.common.ui.LocalNavController

@Composable
fun DebugScreen() {
    val navController = LocalNavController.current

    val context = LocalContext.current

    val viewModel = viewModel(
        key = DebugViewModel.KEY_DEBUG,
        initializer = {
            DebugViewModel(app = context.applicationContext as Application)
        }
    )

    val histories by viewModel.realmDatabase.historiesFlow.collectAsStateWithLifecycle()

    DebugScreenContent(
        histories = histories,
        insertHistoryIntoDatabase = viewModel::insertHistoryIntoDatabase,
        navigateUp = navController::navigateUp
    )
}
