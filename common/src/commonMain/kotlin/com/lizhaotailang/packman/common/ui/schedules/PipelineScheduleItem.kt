package com.lizhaotailang.packman.common.ui.schedules

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PipelineScheduleItem(
    item: PipelineScheduleListItem,
    onClick: (PipelineScheduleListItem) -> Unit
) {
    ListItem(
        headlineText = {
            Text(text = "#${item.id} ${item.description}")
        },
        supportingText = {
            val nextRunAt =
                item.nextRunAt.toLocalDateTime(timeZone = TimeZone.of(zoneId = item.cronTimezone))
            Text(
                text = "${item.ref}\nNext run at $nextRunAt",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable {
                onClick.invoke(item)
            }
    )
}
