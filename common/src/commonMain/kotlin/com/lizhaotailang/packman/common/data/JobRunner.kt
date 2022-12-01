package com.lizhaotailang.packman.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobRunner(

    val id: Long,

    val description: String,

    @SerialName("ip_address")
    val ipAddress: String,

    val active: Boolean,

    @SerialName("is_shared")
    val isShared: Boolean,

    val name: String,

    val status: String

)
