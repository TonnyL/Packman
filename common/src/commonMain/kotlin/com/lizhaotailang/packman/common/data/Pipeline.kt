package com.lizhaotailang.packman.common.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pipeline(

    val id: Long,

    @SerialName("project_id")
    val projectId: Int,

    val sha: String,

    val ref: String,

    val status: PipelineStatus,

    @Contextual
    @SerialName("created_at")
    val createdAt: Instant,

    @Contextual
    @SerialName("updated_at")
    val updatedAt: Instant,

    @SerialName("web_url")
    val webUrl: String

)
