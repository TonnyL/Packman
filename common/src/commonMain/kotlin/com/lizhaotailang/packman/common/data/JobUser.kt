package com.lizhaotailang.packman.common.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobUser(

    val id: Long,

    val name: String?,

    val username: String,

    val state: String,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("web_url")
    val webUrl: String,

    @SerialName("created_at")
    val createdAt: Instant,

    val bio: String?,

    val location: String?,

    @SerialName("public_email")
    val publicEmail: String?,

    val skype: String?,

    val linkedin: String?,

    val twitter: String?,

    @SerialName("website_url")
    val websiteUrl: String?,

    val organization: String?

)
