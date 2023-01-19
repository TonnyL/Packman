package com.lizhaotailang.packman.android.schedules

import android.app.Application
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.android.ErrorScreen
import com.lizhaotailang.packman.android.HomeViewModel
import com.lizhaotailang.packman.android.LocalNavController
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.ui.Screen
import com.lizhaotailang.packman.common.ui.schedules.PipelineScheduleItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PipelineSchedulesScreen(innerPaddings: PaddingValues) {
    val context = LocalContext.current

    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel(app = context.applicationContext as Application)
        }
    )

    val pipelineSchedules by viewModel.allPipelineSchedulesFlow.collectAsStateWithLifecycle()

    val refreshing = pipelineSchedules.status == Status.LOADING
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = viewModel::getAllPipelineSchedules
    )

    when (pipelineSchedules.status) {
        Status.FAILED -> {
            ErrorScreen(action = viewModel::getAllPipelineSchedules)
        }
        Status.SUCCEEDED,
        Status.LOADING -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(state = pullRefreshState)
            ) {
                PipelineSchedulesScreenContent(
                    innerPaddings = innerPaddings,
                    schedules = pipelineSchedules.data.orEmpty()
                )

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

@Composable
private fun PipelineSchedulesScreenContent(
    innerPaddings: PaddingValues,
    schedules: List<PipelineScheduleListItem>
) {
    val navController = LocalNavController.current

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
                onClick = {
                    navController.navigate(
                        route = Screen.ScheduleScreen.route
                            .replace(
                                "{${Screen.ARG_PIPELINE_SCHEDULE_ID}}",
                                it.id.toString()
                            )
                    )
                }
            )
        }

        item(key = "__bottom_padding__") {
            Spacer(modifier = Modifier.height(height = innerPaddings.calculateBottomPadding()))
        }
    }
}
