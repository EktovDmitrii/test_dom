package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @SerializedName("authType")
    val authType: String?,

    @SerializedName("details")
    val details: UserDetailsResponse
)