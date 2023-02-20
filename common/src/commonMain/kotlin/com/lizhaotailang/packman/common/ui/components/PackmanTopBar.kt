package com.lizhaotailang.packman.common.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.lizhaotailang.packman.common.ui.barsBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PackmanTopBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = barsBackground())
) {
    CenterAlignedTopAppBar(
        title = title,
        colors = colors,
        actions = actions,
        navigationIcon = navigationIcon
    )
}
