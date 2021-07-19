package com.custom.rgs_android_dom.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftwareKeyboard(delay: Long = 300L, action: (() -> Unit)? = null) {
    val view = findViewById<View>(android.R.id.content)

    getInputMethodManager()
        .hideSoftInputFromWindow(view.windowToken, 0)

    action?.let { view?.postDelayed(it, delay) }
}

fun Activity.hideSoftwareKeyboard(view: View) {
    getInputMethodManager().hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view: View) =
    getInputMethodManager().showSoftInput(view, 0)

fun Activity.showKeyboardForced() {
    getInputMethodManager().toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
}