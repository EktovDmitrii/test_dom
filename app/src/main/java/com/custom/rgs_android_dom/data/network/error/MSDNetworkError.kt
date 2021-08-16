package com.custom.rgs_android_dom.data.network.error

import com.google.gson.annotations.SerializedName

data class MSDNetworkError(

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String

)