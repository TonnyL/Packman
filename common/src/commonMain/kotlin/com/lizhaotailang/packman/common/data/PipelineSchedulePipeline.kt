package com.lizhaotailang.packman.common.data

import kotlinx.serialization.Serializable

@Serializable
data class PipelineSchedulePipeline(

    val id: Long = 0L,

    val sha: String = "",

    val ref: String = "",

    val status: PipelineStatus

)
