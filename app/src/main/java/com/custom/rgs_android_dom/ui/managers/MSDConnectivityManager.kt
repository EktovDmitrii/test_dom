package com.custom.rgs_android_dom.ui.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.custom.rgs_android_dom.utils.isInternetConnected
import io.reactivex.subjects.BehaviorSubject

class MSDConnectivityManager(private val context: Context) {

    private val connectivityManager: ConnectivityManager
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val connectivitySubject = BehaviorSubject.create<Boolean>()

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                connectivitySubject.onNext(true)
            }

            override fun onLost(network: Network) {
                connectivitySubject.onNext(false)
            }

        })
    }

    fun isInternetConnected(): Boolean{
        return context.isInternetConnected()
    }
}