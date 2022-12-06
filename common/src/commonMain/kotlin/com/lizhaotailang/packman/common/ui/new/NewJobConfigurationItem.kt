package com.lizhaotailang.packman.common.ui.new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.data.toInstant
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.ui.FlowRow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewJobConfigurationItem(
    branchState: MutableState<String>,
    selectedVariants: SnapshotStateList<Variant>,
    requestState: State<Resource<Pipeline>?>,
    onRunClick: (String, List<Variant>) -> Unit
) {
    Column(modifier = Modifier.padding(all = 16.dp)) {
        OutlinedTextField(
            value = branchState.value,
            onValueChange = {
                branchState.value = it
            },
            label = {
                Text(text = "Branch")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Variants",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            Variant.values().forEachIndexed { index, variant ->
                val selected = selectedVariants.contains(variant)

                @Composable
                fun Chip() {
                    FilterChip(
                        selected = selected,
                        onClick = {
                            if (selected) {
                                selectedVariants.remove(variant)
                            } else {
                                selectedVariants.add(variant)
                            }
                        },
                        label = {
                            Text(text = variant.variant)
                        }
                    )
                }

                if (index != Variant.values().size - 1) {
                    Row {
                        Chip()
                        Spacer(modifier = Modifier.width(width = 16.dp))
                    }
                } else {
                    Chip()
                }
            }
        }
        Spacer(modifier = Modifier.height(height = 16.dp))
        FilledTonalButton(
            enabled = branchState.value.trim().isNotEmpty()
                && selectedVariants.isNotEmpty()
                && requestState.value?.status != Status.LOADING,
            onClick = {
                onRunClick.invoke(branchState.value.trim(), selectedVariants)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
        ) {
            Text(text = "Run")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(
    history: History,
    onClick: (History) -> Unit
) {
    ListItem(
        headlineText = {
            Text(text = history.branch)
        },
        supportingText = {
            val variants = history.variants.map { Variant.values()[it] }.joinToString()
            val startedAt = history.startedAt.toInstant()
                .toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
            Text(text = "${variants}\n$startedAt")
        },
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable {
                onClick.invoke(history)
            }
    )
}
