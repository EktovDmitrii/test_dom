package com.custom.rgs_android_dom.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

fun Fragment.showSoftwareKeyboard(view: View) {
    activity?.showKeyboard(view)
}

fun Fragment.showSoftwareKeyboardForced() {
    requireActivity().showKeyboardForced()
}

fun Fragment.hideSoftwareKeyboard() {
    activity?.hideSoftwareKeyboard()
}

fun Fragment.hideSoftwareKeyboard(removeCurrentFocus: Boolean) {
    if (removeCurrentFocus){
        requireActivity().currentFocus?.clearFocus()
    }
    activity?.hideSoftwareKeyboard()
}

fun Fragment.hideSoftwareKeyboard(view: View) = activity?.hideSoftwareKeyboard(view)

fun Fragment.hideSoftwareKeyboard(delay: Long = 300L, action: (() -> Unit)? = null) {
    hideSoftwareKeyboard()

    action?.let { view?.postDelayed(it, delay) }
}
inline fun <T : Fragment> T.args(builder: Bundle.() -> Unit): T {
    arguments = arguments ?: Bundle()
        .apply(builder)
    return this
}