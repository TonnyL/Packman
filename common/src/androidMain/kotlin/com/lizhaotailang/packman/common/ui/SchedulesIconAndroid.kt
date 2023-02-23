package com.lizhaotailang.packman.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.lizhaotailang.packman.common.R

@Composable
actual fun SchedulesIcon(): Painter {
    return painterResource(id = R.drawable.schedules)
}
