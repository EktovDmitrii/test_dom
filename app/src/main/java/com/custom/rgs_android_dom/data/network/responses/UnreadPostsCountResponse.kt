package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class UnreadPostsCountResponse(

    @SerializedName("count")
    val count: Int?

)