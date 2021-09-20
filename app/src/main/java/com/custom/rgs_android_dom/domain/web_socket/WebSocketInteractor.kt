package com.custom.rgs_android_dom.domain.web_socket

import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository

class WebSocketInteractor(
    private val webSocketRepository: WebSocketRepository,
    private val authSharedPreferences: AuthSharedPreferences
){

    fun connect(){
        if (authSharedPreferences.isAuthorized()){
            webSocketRepository.connect()
        }
    }

}