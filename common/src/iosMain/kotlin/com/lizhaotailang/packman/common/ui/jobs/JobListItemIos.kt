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
                    "status_canceled.svg"
                }
                CiJobStatus.CREATED -> {
                    "status_created.svg"
                }
                CiJobStatus.FAILED -> {
                    if (allowFailure) {
                        "status_warning.svg"
                    } else {
                        "status_failed.svg"
                    }
                }
                CiJobStatus.MANUAL -> {
                    "status_manual.svg"
                }
                CiJobStatus.PENDING -> {
                    "status_pending.svg"
                }
                CiJobStatus.PREPARING,
                CiJobStatus.WAITING_FOR_RESOURCE -> {
                    "status_preparing.svg"
                }
                CiJobStatus.RUNNING -> {
                    "status_running.svg"
                }
                CiJobStatus.SCHEDULED -> {
                    "status_scheduled.svg"
                }
                CiJobStatus.SKIPPED -> {
                    "status_skipped.svg"
                }
                CiJobStatus.SUCCESS -> {
                    "status_success.svg"
                }
                CiJobStatus.UNKNOWN__,
                null -> {
                    "status_notfound.svg"
                }
            }
        )
    )
}

@Composable
internal actual fun CiJob.controllerIcon(): Painter {
    return BitmapPainter(image = imageResource(id = ""))
}
