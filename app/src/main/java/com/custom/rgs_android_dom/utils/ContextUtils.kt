package com.custom.rgs_android_dom.utils

import android.content.Context
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