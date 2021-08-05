package com.custom.rgs_android_dom.domain.registration.models

import org.joda.time.DateTime

data class AuthModel(
    val accessToken: String,
    val accessTokenExpiresAt: DateTime,
    val refreshToken: String,
    val refreshTokenExpiresAt: DateTime,
    val sessionId: String
)
