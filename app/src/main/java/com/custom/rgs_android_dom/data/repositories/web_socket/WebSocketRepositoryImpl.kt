package com.custom.rgs_android_dom.data.repositories.web_socket

import android.content.Context
import android.util.Log
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import com.custom.rgs_android_dom.domain.web_socket.models.WsEventModel
import com.custom.rgs_android_dom.ui.managers.AuthContentProviderManager
import com.custom.rgs_android_dom.utils.WsResponseParser
import com.google.gson.Gson
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor

class WebSocketRepositoryImpl(
    private val clientSharedPreferences: ClientSharedPreferences,
    private val gson: Gson,
    private val context: Context
) : WebSocketRepository {

    companion object {
        private const val TAG = "MSDWebSocket"

        private const val CLOSE_REASON_NORMAL = 1000
    }

    var isConnected = false

    val newMessageSubject: PublishSubject<WsEventModel<*>> = PublishSubject.create()

    private var webSocket: WebSocket? = null
    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "ON OPEN")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "ON MESSAGE " + text)
            val parsedMessage = wsResponseParser.parse(text, clientSharedPreferences.getClient()?.userId ?: "")
            if (parsedMessage != null){
                newMessageSubject.onNext(parsedMessage)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "ON CLOSED " + reason)
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            Log.d(TAG, "ON failure")
            throwable.printStackTrace()
        }
    }

    private val wsResponseParser = WsResponseParser(gson)

    override fun connect(){

        val token = AuthContentProviderManager.getAccessToken(context)
        Log.d(TAG, "CONNECTING TO SOCKET " + token)
        if (token != null){
            val wsUrl = BuildConfig.WS_URL.replace("%s", token)
            val clientBuilder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }

            webSocket = clientBuilder
                //.pingInterval(10, TimeUnit.SECONDS)
                .build()
                .newWebSocket(
                    Request.Builder().url(wsUrl).build(),
                    webSocketListener
                )

            isConnected = true

            Log.d(TAG, "CONNECTING")
        }

    }

    override fun disconnect(){
        isConnected = false
        webSocket?.close(CLOSE_REASON_NORMAL, null)
    }

    override fun getWsNewMessageSubject(): PublishSubject<WsEventModel<*>> {
        return newMessageSubject
    }

}