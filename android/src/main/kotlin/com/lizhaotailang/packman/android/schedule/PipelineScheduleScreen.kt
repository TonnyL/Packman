package com.lizhaotailang.packman.android.schedule

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.android.ErrorScreen
import com.lizhaotailang.packman.android.LocalNavController
import com.lizhaotailang.packman.android.PackmanTopBar
import com.lizhaotailang.packman.common.network.Status
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PipelineScheduleScreen(pipelineScheduleId: Int) {
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            PackmanTopBar(
                title = {
                    Text(text = "Schedule")
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
        }
    ) { paddingValues ->
        PipelineScheduleScreenContent(
            innerPaddings = paddingValues,
            pipelineScheduleId = pipelineScheduleId
        )
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
private fun PipelineScheduleScreenContent(
    innerPaddings: PaddingValues,
    pipelineScheduleId: Int
) {
    val viewModel = viewModel(
        initializer = {
            PipelineScheduleViewModel(pipelineScheduleId = pipelineScheduleId)
        }
    )

    val pipelineScheduleResource by viewModel.getASinglePipelineScheduleFlow.collectAsStateWithLifecycle()

    when (pipelineScheduleResource.status) {
        Status.SUCCEEDED -> {
            val schedule = pipelineScheduleResource.data
            if (schedule != null) {
                LazyColumn(contentPadding = innerPaddings) {
                    item {
                        Text(
                            text = "#$pipelineScheduleId ${schedule.description}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp
                            )
                        )
                    }

                    pipelineScheduleInfoItem(
                        headline = "Status",
                        supporting = if (schedule.active) {
                            "active"
                        } else {
                            "inactive"
                        }
                    )

                    pipelineScheduleInfoItem(
                        headline = "Target",
                        supporting = schedule.ref
                    )

                    val nextRunAt =
                        schedule.nextRunAt.toLocalDateTime(timeZone = TimeZone.of(zoneId = schedule.cronTimezone))
                    pipelineScheduleInfoItem(
                        headline = "Next run",
                        supporting = nextRunAt.toString()
                    )

                    pipelineScheduleInfoItem(
                        headline = "Cron",
                        supporting = schedule.cron
                    )

                    pipelineScheduleInfoItem(
                        headline = "Cron timezone",
                        supporting = schedule.cronTimezone
                    )

                    pipelineScheduleInfoItem(
                        headline = "Owner",
                        supporting = "${schedule.owner?.username}(${schedule.owner?.name})"
                    )

                    if (schedule.variables.isNotEmpty()) {
                        pipelineScheduleInfoItem(
                            headline = "Variables",
                            supporting = schedule.variables.joinToString { "${it.key}: ${it.value}" }
                        )
                    }
                }
            }
        }
        Status.FAILED -> {
            ErrorScreen(action = viewModel::getASinglePipelineSchedule)
        }
        Status.LOADING -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.pipelineScheduleInfoItem(
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
