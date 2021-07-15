package com.custom.rgs_android_dom.utils

import android.view.View
import androidx.constraintlayout.widget.Group

private const val DEBOUNCE: Long = 300L
private var isClickEnabled: Boolean = true
private val enable: () -> Unit = { isClickEnabled = true }

fun View.setOnDebouncedClickListener(action: () -> Unit) {
    this.setOnClickListener { v ->
        if (isClickEnabled) {
            isClickEnabled = false
            v.postDelayed(enable, DEBOUNCE)
            action.invoke()
        }
    }
}

fun Group.setOnClickListener(view: View?, action: () -> Unit) {
    referencedIds.forEach {
        view?.findViewById<View>(it)?.setOnDebouncedClickListener(action::invoke)
    }
}

