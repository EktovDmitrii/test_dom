package com.custom.rgs_android_dom.utils

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager

fun Context.getInputMethodManager() =
    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

fun Context.vibratePhone() {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(200)
    }
}


fun Context.editSharedPrefs(name: String, action: (SharedPreferences.Editor) -> Unit) =
    getSharedPrefs(name)
        .edit()
        .apply { action(this) }
        .apply()

fun Context.getSharedPrefs(name: String): SharedPreferences =
    getSharedPreferences(name, Context.MODE_PRIVATE)

fun Context.getDownloadManager() =
    getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

