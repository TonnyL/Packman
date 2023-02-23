package com.lizhaotailang.packman.common.ui.schedules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.ui.ErrorScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun PipelineSchedulesScreenContent(
    innerPaddings: PaddingValues,
    pipelineSchedules: Resource<List<PipelineScheduleListItem>>,
    schedules: List<PipelineScheduleListItem>,
    navigate: (PipelineScheduleListItem) -> Unit,
    onRefresh: () -> Unit
) {
    val refreshing = pipelineSchedules.status == Status.LOADING

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = onRefresh
    )

    when (pipelineSchedules.status) {
        Status.FAILED -> {
            ErrorScreen(action = onRefresh)
        }
        Status.SUCCEEDED,
        Status.LOADING -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(state = pullRefreshState)
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item(key = "__top_padding__") {
                        Spacer(modifier = Modifier.height(height = innerPaddings.calculateTopPadding()))
                    }

                    items(
                        items = schedules,
                        key = {
                            it.id
                        }
                    ) {
                        PipelineScheduleItem(
                            item = it,
                            onClick = { item ->
                                navigate.invoke(item)
                            }
                        )
                    }

                    item(key = "__bottom_padding__") {
                        Spacer(modifier = Modifier.height(height = innerPaddings.calculateBottomPadding()))
                    }
                }

                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = innerPaddings.calculateTopPadding())
                )
            }
        }
    }
}
