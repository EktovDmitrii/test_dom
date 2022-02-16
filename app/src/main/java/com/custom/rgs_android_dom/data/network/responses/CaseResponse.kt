package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

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
    val unreadPosts: Int
)