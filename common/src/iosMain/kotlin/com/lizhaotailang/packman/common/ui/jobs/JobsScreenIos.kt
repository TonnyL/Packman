package com.lizhaotailang.packman.common.ui.jobs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.ui.ErrorScreen
import com.lizhaotailang.packman.common.ui.HomeViewModel
import com.lizhaotailang.packman.graphql.fragment.CiJob

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun JobsScreen(
    innerPaddings: PaddingValues,
    viewModel: HomeViewModel,
    navigateToJobDetails: (CiJob) -> Unit
) {
    val jobs by viewModel.jobsFlow.collectAsState()

    val isRefreshing = jobs?.status == Status.LOADING
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refreshJobs
    )

    when (jobs?.status) {
        Status.FAILED -> {
            ErrorScreen(action = viewModel::refreshJobs)
        }
        Status.SUCCEEDED,
        Status.LOADING,
        null -> {
            if (jobs?.status == Status.SUCCEEDED
                && jobs?.data.isNullOrEmpty()
            ) {
                ErrorScreen(action = viewModel::refreshJobs)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(state = pullRefreshState)
                ) {
                    CiJobsScreenContent(
                        innerPaddings = innerPaddings,
                        jobs = jobs?.data.orEmpty(),
                        navigateToJobDetails = navigateToJobDetails
                    )

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
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
}
