package com.lizhaotailang.packman.common.ui.jobs

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lizhaotailang.packman.graphql.fragment.CiJob
import com.lizhaotailang.packman.graphql.type.CiJobStatus
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListItem(
    job: CiJob,
    navigate: (CiJob) -> Unit
) {
    ListItem(
        headlineText = {
            Text(
                text = "${job.id} ${job.refName}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingText = {
            val duration = job.duration?.let {
                String.format(" • %02d:%02d", (it % 3600) / 60, (it % 60))
            } ?: ""

            val username = job.pipeline?.pipeline?.user?.userCore?.username ?: "ghost"
            val created = job.createdAt.toLocalDateTime(TimeZone.of("Asia/Shanghai"))
            Text(
                text = "${job.name} by ${username}\n${created}$duration",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            Box(modifier = Modifier.size(size = 40.dp)) {
                Surface(
                    modifier = Modifier
                        .size(size = 40.dp)
                        .clip(shape = CircleShape)
                        .border(
                            width = 1.dp,
                            color = job.color.copy(alpha = .8f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = job.statusIcon(),
                        contentDescription = job.status?.name,
                        tint = job.color
                    )
                }

                if (job.triggered == true) {
                    Icon(
                        painter = job.controllerIcon(),
                        contentDescription = "Triggered",
                        tint = job.color,
                        modifier = Modifier
                            .align(alignment = Alignment.BottomEnd)
                            .size(size = 16.dp)
                            .offset(x = 4.dp, y = 2.dp)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable {
                navigate.invoke(job)
            }
    )
}

private val CiJob.color: Color
    get() = when (status) {
        CiJobStatus.CANCELED,
        CiJobStatus.MANUAL -> {
            Color(red = 92, green = 92, blue = 92)
        }
        CiJobStatus.CREATED,
        CiJobStatus.SKIPPED -> {
            Color(red = 143, green = 143, blue = 143)
        }
        CiJobStatus.FAILED -> {
            if (allowFailure) {
                Color(red = 244, green = 146, blue = 62)
            } else {
                Color(red = 199, green = 61, blue = 84)
            }
        }
        CiJobStatus.PENDING -> {
            Color(red = 244, green = 146, blue = 62)
        }
        CiJobStatus.RUNNING -> {
            Color(red = 68, green = 158, blue = 212)
        }
        CiJobStatus.SUCCESS -> {
            Color(red = 81, green = 171, blue = 107)
        }
        CiJobStatus.PREPARING,
        CiJobStatus.WAITING_FOR_RESOURCE,
        CiJobStatus.SCHEDULED,
        CiJobStatus.UNKNOWN__,
        null -> {
            Color(red = 81, green = 171, blue = 107)
        }
    }

@Composable
expect fun CiJob.statusIcon(): Painter

@Composable
expect fun CiJob.controllerIcon(): Painter
