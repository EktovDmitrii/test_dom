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

infix fun View.visibleIf(expr: Boolean) {
    visibility = if (expr) View.VISIBLE else View.GONE
}

infix fun View.invisibleIf(expr: Boolean) {
    visibility = if (expr) View.INVISIBLE else View.VISIBLE
}
