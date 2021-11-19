package com.custom.rgs_android_dom.data.network.url

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object DownloadManagerRequestProvider : KoinComponent {

    private const val AUTHORIZATION_HEADER = "Authorization"
    private const val AUTHORIZATION_BEARER = "Bearer"

    private val registrationRepository: RegistrationRepository by inject()

    fun makeDownloadManagerRequest(url: String, fileName: String, title: String, description: String): DownloadManager.Request {
        return DownloadManager.Request(Uri.parse(url))
            .addRequestHeader(AUTHORIZATION_HEADER, "$AUTHORIZATION_BEARER ${registrationRepository.getAccessToken()}")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(title)
            .setDescription(description)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
    }

}