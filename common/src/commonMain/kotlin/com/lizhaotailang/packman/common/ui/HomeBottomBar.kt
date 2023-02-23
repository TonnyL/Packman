package com.lizhaotailang.packman.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
internal fun HomeBottomBar(
    selectedItemState: MutableState<MainScreenNavigationItem>
) {
    Box(modifier = Modifier.background(color = barsBackground())) {
        NavigationBar(containerColor = Color.Transparent) {
            MainScreenNavigationItem.values().forEach { item ->
                NavigationBarItem(
                    selected = selectedItemState.value == item,
                    onClick = {
                        selectedItemState.value = item
                    },
                    icon = {
                        when (item) {
                            MainScreenNavigationItem.Jobs,
                            MainScreenNavigationItem.New -> {
                                Icon(
                                    imageVector = if (item == MainScreenNavigationItem.Jobs) {
                                        Icons.Outlined.List
                                    } else {
                                        Icons.Outlined.Edit
                                    },
                                    contentDescription = item.item
                                )
                            }
                            MainScreenNavigationItem.Schedules -> {
                                Icon(
                                    painter = SchedulesIcon(),
                                    contentDescription = null,
                                    modifier = Modifier.size(size = 24.0.dp)
                                )
                            }
                        }
                    },
                    label = {
                        Text(text = item.item)
                    }
                )
            }
        }
    }
}

@Composable
internal expect fun SchedulesIcon(): Painter
