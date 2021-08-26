package com.custom.rgs_android_dom.utils

import android.util.Log
import com.custom.rgs_android_dom.BuildConfig

fun logException(obj: Any, throwable: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.w(obj.javaClass.simpleName, throwable)
    } else {
        //TODO Add remote exception hadling
        //FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}