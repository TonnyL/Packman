package com.lizhaotailang.packman.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.lizhaotailang.packman.common.util.imageResource

@Composable
internal actual fun SchedulesIcon(): Painter {
    return BitmapPainter(image = imageResource(id = "schedules"))
}
