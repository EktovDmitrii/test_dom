package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class TaskModificationResponse(

    @SerializedName("taskId")
    val taskId: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("subStatus")
    val subStatus: String
)