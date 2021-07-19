package com.custom.rgs_android_dom.utils

import android.widget.EditText

fun EditText.focus() {
    isFocusableInTouchMode = true
    isFocusable = true
    requestFocus()
}

fun EditText.unFocus() {
    isFocusableInTouchMode = false
    isFocusable = false
}