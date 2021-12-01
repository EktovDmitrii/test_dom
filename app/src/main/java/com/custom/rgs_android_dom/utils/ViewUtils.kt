package com.custom.rgs_android_dom.utils

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.Px

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

fun View.getLocationOnScreen(): Point {
    val rect = Rect()
    getGlobalVisibleRect(rect)
    return Point(rect.left, rect.bottom)
}

inline fun  View.afterMeasured(crossinline measuredCallback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                measuredCallback.invoke()
            }
        }
    })
}

fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        top?.let {
            params.topMargin = it
        }
        left?.let {
            params.leftMargin = it
        }
        right?.let {
            params.rightMargin = it
        }
        bottom?.let {
            params.bottomMargin = it
        }
        requestLayout()
    }
}