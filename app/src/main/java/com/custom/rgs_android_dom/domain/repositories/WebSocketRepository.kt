package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.web_socket.models.WsEventModel
import io.reactivex.subjects.PublishSubject

interface WebSocketRepository {

    fun connect()

    fun disconnect()

    fun getWsNewMessageSubject(): PublishSubject<WsEventModel<*>>

}