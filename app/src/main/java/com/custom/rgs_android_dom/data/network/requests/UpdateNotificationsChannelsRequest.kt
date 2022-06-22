package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateNotificationsChannelsRequest(

    @SerializedName("channels")
    val channels: List<UpdateNotificationChannelRequest>,

)