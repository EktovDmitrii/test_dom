package com.custom.rgs_android_dom.ui.managers

import android.content.Context
import android.net.ConnectivityManager

class ConnectionManager(private val context: Context) {

    fun isInternetConnected(): Boolean {
        var status = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?
        if (cm != null) {
            if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
                status = true
            }
        }

        return status
    }
}