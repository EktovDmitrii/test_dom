package com.custom.rgs_android_dom.data.network.url

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object GlideUrlProvider : KoinComponent {

    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val AUTHORIZATION_BEARER = "Bearer"

    private val registrationRepository: RegistrationRepository by inject()

    fun makeAvatarGlideUrl(avatar: String): GlideUrl{
        return GlideUrl(
            avatar,
            LazyHeaders.Builder()
                .addHeader(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER ${registrationRepository.getAccessToken()}")
                .build()
        )
    }

}