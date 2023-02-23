package com.lizhaotailang.packman.common.ui.job

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
import com.lizhaotailang.packman.common.ui.PackmanTheme
import com.lizhaotailang.packman.common.ui.Screen

@Composable
internal fun JobScreen(
    insets: WindowInsets,
    jobId: String,
    cancelable: Boolean,
    retryable: Boolean,
    triggered: Boolean,
    currentScreenState: MutableState<Screen>
) {
    PackmanTheme {
        Column {
            TopPadding(insets = insets)

            val viewModel = remember {
                JobViewModel(
                    jobId = jobId,
                    cancelable = cancelable,
                    retryable = retryable
                )
            }

            val cancelStatus by viewModel.cancelStatusFlow.collectAsState()
            val retryStatus by viewModel.retryStatusFlow.collectAsState()
            val jobResource by viewModel.jobResourceFlow.collectAsState()
            val variantsString by viewModel.variantsFlow.collectAsState()
            val snackbarMessage by viewModel.snackbarMessageFlow.collectAsState()

            Box(modifier = Modifier.weight(weight = 1f)) {
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
                    navigateUp = {
                        currentScreenState.value = Screen.HomeScreen
                    },
                    clearSnackbarMessage = viewModel::clearSnackbarMessage
                )
            }

            BottomPadding(insets = insets)
        }
    }
}
