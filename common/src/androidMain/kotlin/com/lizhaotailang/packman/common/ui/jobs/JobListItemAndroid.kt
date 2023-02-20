package com.lizhaotailang.packman.common.ui.jobs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.lizhaotailang.packman.common.R
import com.lizhaotailang.packman.graphql.fragment.CiJob
import com.lizhaotailang.packman.graphql.type.CiJobStatus

@Composable
internal actual fun CiJob.statusIcon(): Painter {
    return painterResource(
        id = when (status) {
            CiJobStatus.CANCELED -> {
                R.drawable.status_canceled
            }
            CiJobStatus.CREATED -> {
                R.drawable.status_created
            }
            CiJobStatus.FAILED -> {
                if (allowFailure) {
                    R.drawable.status_warning
                } else {
                    R.drawable.status_failed
                }
            }
            CiJobStatus.MANUAL -> {
                R.drawable.status_manual
            }
            CiJobStatus.PENDING -> {
                R.drawable.status_pending
            }
            CiJobStatus.PREPARING,
            CiJobStatus.WAITING_FOR_RESOURCE -> {
                R.drawable.status_preparing
            }
            CiJobStatus.RUNNING -> {
                R.drawable.status_running
            }
            CiJobStatus.SCHEDULED -> {
                R.drawable.status_scheduled
            }
            CiJobStatus.SKIPPED -> {
                R.drawable.status_skipped
            }
            CiJobStatus.SUCCESS -> {
                R.drawable.status_success
            }
            CiJobStatus.UNKNOWN__,
            null -> {
                R.drawable.status_notfound
            }
        }
    )
}

@Composable
internal actual fun CiJob.controllerIcon(): Painter {
    return painterResource(id = R.drawable.stadia_controller)
}
