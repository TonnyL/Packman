package com.lizhaotailang.packman.common.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PipelineScheduleListItem(

    val id: Int = 0,

    val description: String = "",

    val ref: String = "",

    val cron: String = "",

    @SerialName("cron_timezone")
    val cronTimezone: String = "",

    @SerialName("next_run_at")
    val nextRunAt: Instant = Instant.DISTANT_FUTURE,

    val active: Boolean = false,

    @SerialName("created_at")
    val createdAt: Instant = Instant.DISTANT_PAST,

    @SerialName("updated_at")
    val updatedAt: Instant = Instant.DISTANT_PAST,

    val owner: PipelineScheduleOwner? = null

)
