package com.lizhaotailang.packman.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PipelineScheduleOwner(

    val name: String = "",

    val username: String = "",

    val id: Int = 0,

    val state: String = "",

    @SerialName("avatar_url")
    val avatarUrl: String = "",

    @SerialName("web_url")
    val webUrl: String = ""

)
