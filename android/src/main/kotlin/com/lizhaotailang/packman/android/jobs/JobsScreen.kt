package com.lizhaotailang.packman.android.jobs

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.lizhaotailang.packman.android.CiJobPreviewProviders
import com.lizhaotailang.packman.android.HomeViewModel
import com.lizhaotailang.packman.android.LocalNavController
import com.lizhaotailang.packman.android.R
import com.lizhaotailang.packman.common.ui.Screen
import com.lizhaotailang.packman.common.ui.jobs.JobListItem
import com.lizhaotailang.packman.graphql.fragment.CiJob
import com.lizhaotailang.packman.graphql.type.CiJobStatus.*

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 64.dp)
            ) {
                val reload = {
                    jobs.refresh()
                }

                Image(
                    painter = painterResource(id = R.drawable.something_wrong),
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = reload,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                )
                Spacer(modifier = Modifier.height(height = 16.dp))
                TextButton(onClick = reload) {
                    Text(
                        text = "Something went wrong, tap to reload...",
                        textAlign = TextAlign.Center
                    )
                }
            }
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

    LazyColumn(contentPadding = innerPaddings) {
        when (jobs.loadState.refresh) {
            is LoadState.Error -> {
                item(key = "_load_error_") {
                }
            }
            LoadState.Loading -> {

            }
            is LoadState.NotLoading -> {
                itemsIndexed(
                    items = jobs,
                    key = { _, item ->
                        item.id ?: ""
                    }
                ) { _, item ->
                    if (item != null) {
                        JobListItem(
                            job = item,
                            navigate = { job ->
                                job.id?.let {
                                    navController.navigate(
                                        route = Screen.JobScreen.route
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
                }
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
