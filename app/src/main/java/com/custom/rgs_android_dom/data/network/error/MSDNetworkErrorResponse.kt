package com.custom.rgs_android_dom.data.network.error

import com.google.gson.annotations.SerializedName

data class MSDNetworkErrorResponse(

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("translationKey")
    val translationKey: String

)