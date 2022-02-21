package com.custom.rgs_android_dom.data.network.responses

import com.custom.rgs_android_dom.domain.chat.models.CaseStatus
import com.custom.rgs_android_dom.domain.chat.models.CaseSubStatus
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class CaseResponse(
    @SerializedName("channelId")
    val channelId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("subtype")
    val subtype: String,

    @SerializedName("taskId")
    val taskId: String,

    @SerializedName("unreadPosts")
    val unreadPosts: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("subStatus")
    val subStatus: String,

    @SerializedName("reportedAt")
    val reportedAt: DateTime
)