package com.lizhaotailang.packman.common.ui.job

import android.app.Application
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.common.ui.LocalNavController

@Composable
fun JobScreen(
    jobId: String,
    cancelable: Boolean,
    retryable: Boolean,
    triggered: Boolean
) {
    val context = LocalContext.current
    val viewModel = viewModel(
        initializer = {
            JobViewModel(
                jobId = jobId,
                cancelable = cancelable,
                retryable = retryable,
                app = context.applicationContext as Application
            )
        }
    )

    val navController = LocalNavController.current

    val cancelStatus by viewModel.cancelStatusFlow.collectAsStateWithLifecycle()
    val retryStatus by viewModel.retryStatusFlow.collectAsStateWithLifecycle()
    val jobResource by viewModel.jobResourceFlow.collectAsStateWithLifecycle()
    val variantsString by viewModel.variantsFlow.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessageFlow.collectAsStateWithLifecycle()

    JobScreenContent(
        jobId = jobId,
        retryable = retryable,
        cancelable = cancelable,
        triggered = triggered,
        cancelStatus = cancelStatus,
        retryStatus = retryStatus,
        jobResource = jobResource,
        variantsString = variantsString,
        snackbarMessage = snackbarMessage,
        cancel = viewModel::cancel,
        retry = viewModel::retry,
        fetchJobInfo = viewModel::fetchJobInfo,
        navigateUp = navController::navigateUp,
        clearSnackbarMessage = viewModel::clearSnackbarMessage,
        bottomBarModifier = Modifier.navigationBarsPadding()
    )
}
