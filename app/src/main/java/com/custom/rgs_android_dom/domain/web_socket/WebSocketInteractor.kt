package com.custom.rgs_android_dom.domain.web_socket

import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager

class WebSocketInteractor(
    private val webSocketRepository: WebSocketRepository,
    private val authContentProviderManager: AuthContentProviderManager
){

    fun connect(){
        if (authContentProviderManager.isAuthorized()){
            webSocketRepository.connect()
        }
    }

}