package com.custom.rgs_android_dom.utils

import android.util.Log
import com.custom.rgs_android_dom.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun logException(obj: Any, throwable: Throwable) {
    if (BuildConfig.DEBUG) {
        Log.w(obj.javaClass.simpleName, throwable)
    } else {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}