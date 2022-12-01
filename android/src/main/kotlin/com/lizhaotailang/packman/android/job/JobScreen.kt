package com.lizhaotailang.packman.android.job

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.android.ErrorScreen
import com.lizhaotailang.packman.android.LocalNavController
import com.lizhaotailang.packman.android.PackmanTopBar
import com.lizhaotailang.packman.common.data.PipelineStatus
import com.lizhaotailang.packman.common.network.Status

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun JobScreen(
    jobId: String,
    cancelable: Boolean,
    retryable: Boolean,
    triggered: Boolean
) {
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }

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

    Scaffold(
        topBar = {
            PackmanTopBar(
                title = {
                    Text(text = "Job")
                },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .navigationBarsPadding()
            )
        }
    ) { paddingValues ->
        JobScreenContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            jobId = jobId,
            retryable = retryable,
            cancelable = cancelable,
            triggered = triggered
        )

        val snackbarMessage by viewModel.snackbarMessageFlow.collectAsStateWithLifecycle()

        LaunchedEffect(snackbarMessage) {
            if (snackbarMessage.isNotEmpty()) {
                val result = snackbarHostState.showSnackbar(
                    message = snackbarMessage,
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
                when (result) {
                    SnackbarResult.Dismissed -> {
                        viewModel.clearSnackbarMessage()
                    }
                    SnackbarResult.ActionPerformed -> {

                    }
                }
            }
        }
    }
}

@OptIn(
    ExperimentalLifecycleComposeApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
private fun JobScreenContent(
    paddingValues: PaddingValues,
    viewModel: JobViewModel,
    jobId: String,
    cancelable: Boolean,
    retryable: Boolean,
    triggered: Boolean
) {
    val cancelStatus by viewModel.cancelStatusFlow.collectAsStateWithLifecycle()
    val retryStatus by viewModel.retryStatusFlow.collectAsStateWithLifecycle()
    val jobResource by viewModel.jobResourceFlow.collectAsStateWithLifecycle()
    val variantsString by viewModel.variantsFlow.collectAsStateWithLifecycle()

    when (jobResource.status) {
        Status.SUCCEEDED -> {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = paddingValues.calculateTopPadding())
                        .weight(weight = 1f)
                ) {
                    item {
                        Text(
                            text = "#$jobId ${jobResource.data?.ref}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp
                            )
                        )
                        if (triggered) {
                            Box(modifier = Modifier.padding(start = 16.dp)) {
                                FilterChip(
                                    selected = true,
                                    enabled = true,
                                    onClick = { },
                                    label = {
                                        Text(text = "Triggered")
                                    }
                                )
                            }
                        }
                    }

                    jobResource.data?.stage?.let { stage ->
                        jobInfoItem(
                            headline = "Stage",
                            supporting = stage
                        )
                    }

                    jobResource.data?.createdAt?.let { createdAt ->
                        jobInfoItem(
                            headline = "Created at",
                            supporting = createdAt.toString()
                        )
                    }

                    jobResource.data?.startedAt?.let { startedAt ->
                        jobInfoItem(
                            headline = "Started at",
                            supporting = startedAt.toString()
                        )
                    }
                    jobResource.data?.status?.let { status ->
                        jobInfoItem(
                            headline = "Status",
                            supporting = if (status == PipelineStatus.Failed
                                && jobResource.data?.allowFailure == true
                            ) {
                                "${status.name} (allowed to fail)"
                            } else {
                                status.name
                            }
                        )
                    }
                    variantsString?.let { variants ->
                        jobInfoItem(
                            headline = "Variants",
                            supporting = variants
                        )
                    }
                    jobResource.data?.duration?.toInt()?.let { duration ->
                        jobInfoItem(
                            headline = "Duration",
                            supporting = String.format(
                                "%d minutes %d seconds",
                                (duration % 3600) / 60,
                                (duration % 60)
                            )
                        )
                    }
                    jobResource.data?.runner?.let { runner ->
                        jobInfoItem(
                            headline = "Runner",
                            supporting = "#${runner.id} ${runner.description}"
                        )
                    }
                    jobResource.data?.commit?.let { commit ->
                        jobInfoItem(
                            headline = "Commit",
                            supporting = "${commit.shortId} - ${commit.message}"
                        )
                    }
                    jobResource.data?.user?.let { user ->
                        jobInfoItem(
                            headline = "User",
                            supporting = "${user.username}(${user.name})"
                        )
                    }
                }

                if (retryable || cancelable) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    elevation = 2.dp
                                )
                            )
                            .padding(all = 16.dp)
                            .navigationBarsPadding()
                    ) {
                        if (cancelable) {
                            FilledTonalButton(
                                enabled = cancelStatus != Status.LOADING,
                                onClick = viewModel::cancel
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                        if (retryable) {
                            Spacer(modifier = Modifier.width(width = 16.dp))
                            FilledTonalButton(
                                enabled = retryStatus != Status.LOADING,
                                onClick = viewModel::retry
                            ) {
                                Text(text = "Retry")
                            }
                        }
                    }
                }
            }
        }
        Status.FAILED -> {
            ErrorScreen(action = viewModel::fetchJobInfo)
        }
        Status.LOADING -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.jobInfoItem(
    headline: String,
    supporting: String
) {
    item {
        ListItem(
            headlineText = {
                Text(text = headline)
            },
            supportingText = {
                Text(
                    text = supporting,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}
