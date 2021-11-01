package com.custom.rgs_android_dom.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

private const val SHOW_KEYBOARD_DELAY_MS = 100

fun EditText.focus() {
    isFocusableInTouchMode = true
    isFocusable = true
    requestFocus()
}

fun EditText.unFocus() {
    isFocusableInTouchMode = false
    isFocusable = false
}

fun EditText.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.showKeyboardWithDelay() {
    postDelayed({
        this.showKeyboard()
    }, SHOW_KEYBOARD_DELAY_MS.toLong())
}

fun EditText.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun TextInputLayout.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}