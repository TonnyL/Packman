package com.lizhaotailang.packman.common.ui.jobs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import com.lizhaotailang.packman.common.util.imageResource
import com.lizhaotailang.packman.graphql.fragment.CiJob
import com.lizhaotailang.packman.graphql.type.CiJobStatus

@Composable
internal actual fun CiJob.statusIcon(): Painter {
    return BitmapPainter(
        image = imageResource(
            id = when (status) {
                CiJobStatus.CANCELED -> {
                    "status_canceled"
                }
                CiJobStatus.CREATED -> {
                    "status_created"
                }
                CiJobStatus.FAILED -> {
                    if (allowFailure) {
                        "status_warning"
                    } else {
                        "status_failed"
                    }
                }
                CiJobStatus.MANUAL -> {
                    "status_manual"
                }
                CiJobStatus.PENDING -> {
                    "status_pending"
                }
                CiJobStatus.PREPARING,
                CiJobStatus.WAITING_FOR_RESOURCE -> {
                    "status_preparing"
                }
                CiJobStatus.RUNNING -> {
                    "status_running"
                }
                CiJobStatus.SCHEDULED -> {
                    "status_scheduled"
                }
                CiJobStatus.SKIPPED -> {
                    "status_skipped"
                }
                CiJobStatus.SUCCESS -> {
                    "status_success"
                }
                CiJobStatus.UNKNOWN__,
                null -> {
                    "status_notfound"
                }
            }
        )
    )
}

@Composable
internal actual fun CiJob.controllerIcon(): Painter {
    return BitmapPainter(image = imageResource(id = "stadia_controller"))
}
