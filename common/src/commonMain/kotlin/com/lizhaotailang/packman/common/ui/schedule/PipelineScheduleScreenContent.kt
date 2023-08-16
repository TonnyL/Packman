package com.lizhaotailang.packman.common.ui.schedule

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lizhaotailang.packman.common.data.PipelineScheduleDetails
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.ui.ErrorScreen
import com.lizhaotailang.packman.common.ui.components.PackmanTopBar
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PipelineScheduleScreenContent(
    pipelineScheduleResource: Resource<PipelineScheduleDetails>,
    pipelineScheduleId: Int,
    retry: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            PackmanTopBar(
                title = {
                    Text(text = "Schedule")
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPaddings ->
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
                ErrorScreen(action = retry)
            }
            Status.LOADING -> {

            }
        }
    }
}

private fun LazyListScope.pipelineScheduleInfoItem(
    headline: String,
    supporting: String
) {
    item {
        ListItem(
            headlineContent = {
                Text(text = headline)
            },
            supportingContent = {
                Text(
                    text = supporting,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}
