package com.lizhaotailang.packman.common.ui.new

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.network.Resource

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalStdlibApi::class
)
@Composable
internal fun NewJobScreenContent(
    innerPaddings: PaddingValues,
    requestState: State<Resource<Pipeline>?>,
    histories: List<History>,
    onRunClick: (String, List<Variant>) -> Unit,
    deleteHistory: (History) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val branchState = remember { mutableStateOf(value = "") }
    val selectedVariants = remember { mutableStateListOf<Variant>() }

    LazyColumn {
        item(key = "__top_padding") {
            Spacer(modifier = Modifier.height(height = innerPaddings.calculateTopPadding()))
        }

        item(key = "__new_job__") {
            NewJobConfigurationItem(
                branchState = branchState,
                selectedVariants = selectedVariants,
                requestState = requestState,
                onRunClick = { branch, variants ->
                    onRunClick.invoke(branch, variants)

                    keyboardController?.hide()
                }
            )
        }

        if (histories.isNotEmpty()) {
            item(key = "__histories__") {
                Text(
                    text = "Histories",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
        }

        items(
            count = histories.size,
            key = {
                histories[it]._id.toHexString()
            }
        ) { index ->
            val history = histories.getOrNull(index)
            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToEnd
                        || dismissValue == DismissValue.DismissedToStart
                    ) {
                        history?.let {
                            deleteHistory.invoke(it)
                        }

                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismiss(
                state = dismissState,
                background = {
                    HistoryBackground(dismissState = dismissState)
                }
            ) {
                if (history != null) {
                    HistoryItem(
                        history = history,
                        onClick = {
                            branchState.value = history.branch
                            selectedVariants.clear()
                            selectedVariants.addAll(
                                elements = history.variants.map {
                                    Variant.entries[it]
                                }
                            )
                        }
                    )
                }
            }
        }

        item(key = "__bottom_padding") {
            Spacer(modifier = Modifier.height(height = innerPaddings.calculateBottomPadding()))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HistoryBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return

    val backgroundColor by animateColorAsState(MaterialTheme.colorScheme.errorContainer)
    val iconColor by animateColorAsState(MaterialTheme.colorScheme.onErrorContainer)

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) {
            0.75f
        } else {
            1f
        }
    )

    val alignment = when (direction) {
        DismissDirection.StartToEnd -> {
            Alignment.CenterStart
        }
        DismissDirection.EndToStart -> {
            Alignment.CenterEnd
        }
    }

    Box(
        contentAlignment = alignment,
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Localized description",
            tint = iconColor,
            modifier = Modifier.scale(scale)
        )
    }
}
