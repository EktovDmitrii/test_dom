package com.custom.rgs_android_dom.utils.activity

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.custom.rgs_android_dom.utils.getInputMethodManager

fun Activity.hideSoftwareKeyboard() {
    val view = findViewById<View>(android.R.id.content)
    getInputMethodManager().hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideSoftwareKeyboard(view: View) {
    getInputMethodManager().hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view: View) =
    getInputMethodManager().showSoftInput(view, 0)

fun Activity.showKeyboardForced() {
    getInputMethodManager().toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
}

fun Activity.hideKeyboardForced(){
    getInputMethodManager().toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Activity.toast(text: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, text, length).show()