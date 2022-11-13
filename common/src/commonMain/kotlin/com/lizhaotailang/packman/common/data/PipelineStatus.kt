package com.lizhaotailang.packman.common.data

import com.lizhaotailang.packman.graphql.type.CiJobStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @see [CiJobStatus]
 */
@Serializable
enum class PipelineStatus {

    @SerialName("created")
    Created,

    @SerialName("waiting_for_resource")
    WaitingForResource,

    @SerialName("preparing")
    Preparing,

    @SerialName("pending")
    Pending,

    @SerialName("running")
    Running,

    @SerialName("success")
    Success,

    @SerialName("failed")
    Failed,

    @SerialName("canceled")
    Canceled,

    @SerialName("skipped")
    Skipped,

    @SerialName("manual")
    Manual,

    @SerialName("scheduled")
    Scheduled

}
