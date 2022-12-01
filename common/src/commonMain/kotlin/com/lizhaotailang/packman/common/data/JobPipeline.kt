package com.lizhaotailang.packman.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobPipeline(

    val id: Long,

    @SerialName("project_id")
    val projectId: Long,

    val ref: String,

    val sha: String,

    val status: PipelineStatus

)
