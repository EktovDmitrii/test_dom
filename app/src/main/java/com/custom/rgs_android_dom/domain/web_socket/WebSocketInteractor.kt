package com.custom.rgs_android_dom.domain.web_socket

import android.content.Context
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import com.custom.rgs_android_dom.ui.managers.AuthContentProviderManager

class WebSocketInteractor(
    private val webSocketRepository: WebSocketRepository,
    private val context: Context
){

    fun connect(){
        if (AuthContentProviderManager.isAuthorized(context)){
            webSocketRepository.connect()
        }
    }

}