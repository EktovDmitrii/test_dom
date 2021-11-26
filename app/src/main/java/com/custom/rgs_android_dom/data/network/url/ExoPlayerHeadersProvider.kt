package com.custom.rgs_android_dom.data.network.url

import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ExoPlayerDataSourceProvider : KoinComponent {

    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val AUTHORIZATION_BEARER = "Bearer"

    private val registrationRepository: RegistrationRepository by inject()

    fun prepareDataSource(): DataSource.Factory {
        return DefaultHttpDataSource.Factory()
            .setUserAgent("")
            .setDefaultRequestProperties(hashMapOf(AUTHORIZATION_HEADER to "$AUTHORIZATION_BEARER ${registrationRepository.getAccessToken()}"))
    }


}