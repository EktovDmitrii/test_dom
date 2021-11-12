package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ChannelMemberResponse(
    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("middleName")
    val middleName: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("userId")
    val userId: String?
)