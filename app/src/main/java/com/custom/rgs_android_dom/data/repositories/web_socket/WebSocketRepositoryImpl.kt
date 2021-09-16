package com.custom.rgs_android_dom.data.repositories.web_socket

import android.util.Log
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.repositories.WebSocketRepository
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class WebSocketRepositoryImpl(
    private val authSharedPreferences: AuthSharedPreferences
) : WebSocketRepository {

    companion object {
        private const val TAG = "MSDWebSocket"

        private const val CLOSE_REASON_NORMAL = 1000
    }

    var isConnected = false

    private var webSocket: WebSocket? = null
    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "ON OPEN")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "ON MESSAGE " + text)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {

        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            Log.d(TAG, "ON failure")
            throwable.printStackTrace()
        }
    }

    override fun connect(){
        val token = authSharedPreferences.getAccessToken()
        if (token != null){
            val wsUrl = BuildConfig.WS_URL.replace("%s", token)

            val clientBuilder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }

            webSocket = clientBuilder
                .pingInterval(10, TimeUnit.SECONDS)
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

}