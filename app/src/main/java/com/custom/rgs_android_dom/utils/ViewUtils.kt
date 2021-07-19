package com.custom.rgs_android_dom.utils

import android.view.View

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    visibility = View.VISIBLE
}

infix fun View.goneIf(expr: Boolean) {
    visibility = if (expr) View.GONE else View.VISIBLE
}

infix fun View.invisibleIf(expr: Boolean) {
    visibility = if (expr) View.INVISIBLE else View.VISIBLE
}

val View.isGone: Boolean
    get() = visibility == View.GONE

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE