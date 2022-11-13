package com.lizhaotailang.packman.android.new

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.android.HomeViewModel
import com.lizhaotailang.packman.common.ui.new.NewJobConfigurationItem
import com.lizhaotailang.packman.common.ui.new.Variant

@OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NewJobScreen(innerPaddings: PaddingValues) {
    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel()
        }
    )

    val branchState = remember { mutableStateOf(value = "") }
    val selectedVariants = remember { mutableStateListOf<Variant>() }

    val requestState = viewModel.requestFlow.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    LazyColumn(contentPadding = innerPaddings) {
        item {
            NewJobConfigurationItem(
                branchState = branchState,
                selectedVariants = selectedVariants,
                requestState = requestState,
                onRunClick = { branch, variants ->
                    viewModel.triggerNewPipeline(
                        branch = branch,
                        variants = variants
                    )

                    keyboardController?.hide()
                }
            )
        }
    }
}
