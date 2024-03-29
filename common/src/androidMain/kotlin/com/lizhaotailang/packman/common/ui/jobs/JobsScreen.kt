package com.lizhaotailang.packman.common.ui.jobs

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.lizhaotailang.packman.common.ui.CiJobPreviewProviders
import com.lizhaotailang.packman.common.ui.ErrorScreen
import com.lizhaotailang.packman.common.ui.HomeViewModel
import com.lizhaotailang.packman.common.ui.LocalNavController
import com.lizhaotailang.packman.common.ui.Screen
import com.lizhaotailang.packman.graphql.fragment.CiJob

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JobsScreen(innerPaddings: PaddingValues) {
    val context = LocalContext.current

    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel(app = context.applicationContext as Application)
        }
    )

    val jobs = viewModel.jobsFlow.collectAsLazyPagingItems()

    val refreshing = jobs.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing, jobs::refresh)

    when (jobs.loadState.refresh) {
        is LoadState.Error -> {
            ErrorScreen(action = jobs::refresh)
        }
        LoadState.Loading,
        is LoadState.NotLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(state = pullRefreshState)
            ) {
                CiJobsScreenContent(
                    innerPaddings = innerPaddings,
                    jobs = jobs
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
private fun CiJobsScreenContent(
    innerPaddings: PaddingValues,
    jobs: LazyPagingItems<CiJob>
) {
    val navController = LocalNavController.current

    LazyColumn {
        items(
            count = jobs.itemCount,
            key = jobs.itemKey { it.id ?: "" }
        ) { index ->
            val item = jobs[index]
            if (index == 0) {
                Spacer(modifier = Modifier.height(height = innerPaddings.calculateTopPadding()))
            }

            if (item != null) {
                JobListItem(
                    job = item,
                    navigate = { job ->
                        job.id?.let {
                            navController.navigate(
                                route = Screen.JobScreen.ROUTE
                                    .replace(
                                        "{${Screen.ARG_JOB_ID}}",
                                        it.replace("#", "")
                                    )
                                    .replace(
                                        "{${Screen.ARG_RETRYABLE}}",
                                        job.retryable.toString()
                                    )
                                    .replace(
                                        "{${Screen.ARG_CANCELABLE}}",
                                        job.cancelable.toString()
                                    )
                                    .replace(
                                        "{${Screen.ARG_TRIGGERED}}",
                                        job.triggered.toString()
                                    )
                            )
                        }
                    }
                )
            }

            if (index == jobs.itemCount - 1) {
                Spacer(modifier = Modifier.height(height = innerPaddings.calculateBottomPadding()))
            }
        }
    }
}

@Preview(
    name = "JobListItemPreview",
    showBackground = false
)
@Composable
private fun JobListItemPreview(
    @PreviewParameter(
        provider = CiJobPreviewProviders::class,
        limit = 1
    )
    job: CiJob
) {
    JobListItem(
        job = job,
        navigate = {}
    )
}
