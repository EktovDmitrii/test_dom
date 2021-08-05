package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.responses.AuthResponse
import com.custom.rgs_android_dom.domain.registration.models.AuthModel

object RegistrationMapper {

    fun responseToAuthModel(response: AuthResponse): AuthModel {
        return AuthModel(
            accessToken = response.accessToken,
            accessTokenExpiresAt = response.accessTokenExpiresAt,
            refreshToken = response.refreshToken,
            refreshTokenExpiresAt = response.refreshTokenExpiresAt,
            sessionId = response.sessionId
        )
    }

}