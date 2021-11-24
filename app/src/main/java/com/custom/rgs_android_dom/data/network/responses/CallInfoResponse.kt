package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.LocalDateTime

data class CallInfoResponse(

    @SerializedName("channelId")
    val channelId: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("initiatorUserId")
    val initiatorUserId: String?,

    @SerializedName("recipientUserId")
    val recipientUserId: String?,

    @SerializedName("taskId")
    val taskId: String?

)