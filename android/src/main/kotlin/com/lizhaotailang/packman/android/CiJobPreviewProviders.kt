package com.lizhaotailang.packman.android

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.lizhaotailang.packman.graphql.fragment.CiJob
import com.lizhaotailang.packman.graphql.fragment.Pipeline
import com.lizhaotailang.packman.graphql.fragment.UserCore
import com.lizhaotailang.packman.graphql.type.CiJobStatus
import com.lizhaotailang.packman.graphql.type.PipelineStatusEnum
import kotlinx.datetime.Clock

class CiJobPreviewProviders : PreviewParameterProvider<CiJob> {

    override val values: Sequence<CiJob>
        get() = sequenceOf(
            CiJob(
                active = true,
                allowFailure = true,
                cancelable = true,
                createdAt = Clock.System.now(),
                duration = null,
                finishedAt = Clock.System.now(),
                name = "CI Job",
                refName = "feature/akiko_fmg",
                id = "",
                retryable = true,
                status = CiJobStatus.RUNNING,
                triggered = true,
                pipeline = CiJob.Pipeline(
                    __typename = "",
                    pipeline = Pipeline(
                        id = "",
                        iid = "666",
                        status = PipelineStatusEnum.RUNNING,
                        createdAt = Clock.System.now(),
                        user = Pipeline.User(
                            __typename = "",
                            userCore = UserCore(
                                name = "lizhaotailang",
                                username = "LIZHAOTAILANG"
                            )
                        ),
                        duration = 3600,
                        complete = false,
                        cancelable = true,
                        retryable = true
                    )
                )
            )
        )

}
