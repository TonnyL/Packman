package com.lizhaotailang.packman.common.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PipelineVariable(

    val key: String = "",

    @SerialName("variable_type")
    val variableType: String = "",

    val value: String = ""

)
