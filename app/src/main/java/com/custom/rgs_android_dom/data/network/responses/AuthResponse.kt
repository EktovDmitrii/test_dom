package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class AuthResponse(
    @SerializedName("token")
    val token: TokenResponse,

    @SerializedName("clientId")
    val clientId: String,

    @SerializedName("isNewClient")
    val isNewUser: Boolean

)

data class TokenResponse(

    @SerializedName("sessionId")
    val sessionId: String,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("accessTokenExpiresAt")
    val accessTokenExpiresAt: DateTime,

    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("refreshTokenExpiresAt")
    val refreshTokenExpiresAt: DateTime,
)
