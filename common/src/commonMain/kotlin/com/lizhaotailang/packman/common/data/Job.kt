package com.lizhaotailang.packman.common.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Job(

    val commit: JobCommit,

    @SerialName("allow_failure")
    val allowFailure: Boolean,

    @SerialName("created_at")
    val createdAt: Instant,

    @SerialName("started_at")
    val startedAt: Instant?,

    @SerialName("finished_at")
    val finishedAt: Instant?,

    val duration: Float?,

    @SerialName("queued_duration")
    val queuedDuration: Float?,

    @SerialName("artifacts_expire_at")
    val artifactsExpireAt: Instant?,

    val id: Long,

    val name: String,

    val pipeline: JobPipeline,

    val ref: String = "",

    val stage: String = "",

    val status: PipelineStatus,

    @SerialName("failure_reason")
    val failureReason: String? = null,

    val tag: Boolean = false,

    @SerialName("web_url")
    val webUrl: String = "",

    val user: JobUser? = null,

    val runner: JobRunner?

)
