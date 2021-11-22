package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDateTime

data class CallInfoResponse(

    @SerializedName("acceptedAt")
    val acceptedAt: LocalDateTime,

    @SerializedName("channelId")
    val channelId: String?,

    @SerializedName("declinedAt")
    val declinedAt: LocalDateTime,

    @SerializedName("id")
    val id: String?,

    @SerializedName("initiatorUserId")
    val initiatorUserId: String?,

    @SerializedName("recipientUserId")
    val recipientUserId: String?,

    @SerializedName("registeredAt")
    val registeredAt: LocalDateTime,

    @SerializedName("taskId")
    val taskId: String?

)