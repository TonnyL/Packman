package com.lizhaotailang.packman.common.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobCommit(

    @SerialName("author_email")
    val authorEmail: String,

    @SerialName("author_name")
    val authorName: String,

    @SerialName("created_at")
    val createdAt: Instant,

    val id: String,

    val message: String,

    @SerialName("short_id")
    val shortId: String,

    val title: String
)
