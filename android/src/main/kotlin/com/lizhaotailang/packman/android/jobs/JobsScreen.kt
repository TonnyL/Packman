package com.lizhaotailang.packman.android.jobs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.lizhaotailang.packman.android.CiJobPreviewProviders
import com.lizhaotailang.packman.android.HomeViewModel
import com.lizhaotailang.packman.common.ui.jobs.JobListItem
import com.lizhaotailang.packman.graphql.fragment.CiJob
import com.lizhaotailang.packman.graphql.type.CiJobStatus.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JobsScreen(innerPaddings: PaddingValues) {
    val viewModel = viewModel(
        key = HomeViewModel.KEY_HOME_SCREEN,
        initializer = {
            HomeViewModel()
        }
    )

    val jobs = viewModel.jobsFlow.collectAsLazyPagingItems()

    val refreshing = jobs.loadState.refresh == LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing, jobs::refresh)

    Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
        CiJobsScreenContent(
            innerPaddings = innerPaddings,
            jobs = jobs
        )

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
            modifier = Modifier.align(alignment = Alignment.TopCenter)
        )
    }
}

@Composable
private fun CiJobsScreenContent(
    innerPaddings: PaddingValues,
    jobs: LazyPagingItems<CiJob>
) {
    LazyColumn(contentPadding = innerPaddings) {
        itemsIndexed(
            items = jobs,
            key = { _, item ->
                item.id ?: ""
            }
        ) { _, item ->
            if (item != null) {
                JobListItem(job = item)
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
    JobListItem(job = job)
}
