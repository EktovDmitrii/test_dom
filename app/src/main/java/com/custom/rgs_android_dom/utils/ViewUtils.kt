package com.custom.rgs_android_dom.utils

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.forEach
import androidx.core.widget.NestedScrollView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.custom.rgs_android_dom.views.MSDGenderSelector
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import com.custom.rgs_android_dom.views.edit_text.MSDLabelIconEditText

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

infix fun View.goneIf(expr: Boolean) {
    visibility = if (expr) View.GONE else View.VISIBLE
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

fun NestedScrollView.smoothScrollTo(view: View) {
    var distance = view.top
    var viewParent = view.parent
    for (i in 0..9) {
        if ((viewParent as View) === this) break
        distance += (viewParent as View).top
        viewParent = viewParent.getParent()
    }
    smoothScrollTo(0, distance)
}

fun View.fadeVisibility(visibility: Int, duration: Long = 400) {
    val transition: Transition = Fade()
    transition.duration = duration
    transition.addTarget(this)
    TransitionManager.beginDelayedTransition(this as ViewGroup, transition)
    this.visibility = visibility
}

fun MSDLabelEditText.setEnabledEditView(
    isNotSaved: Boolean,
    hasPolicies: Boolean,
    hasProducts: Boolean
) {
    val isEnabled = when {
        hasPolicies -> false
        hasProducts -> isNotSaved
        else -> true
    }
    this.isEnabled = isEnabled
}

fun MSDLabelIconEditText.setEnabledIconEditView(
    isNotSaved: Boolean,
    hasPolicies: Boolean,
    hasProducts: Boolean
) {
    val isEnabled = when {
        hasPolicies -> false
        hasProducts -> isNotSaved
        else -> true
    }
    this.isEnabled = isEnabled
}

fun MSDGenderSelector.setEnabledSelectView(
    isNotSaved: Boolean,
    hasPolicies: Boolean,
    hasProducts: Boolean
) {
    val isEnabled = when {
        hasPolicies -> false
        hasProducts -> isNotSaved
        else -> true
    }
    this.isEnabled = isEnabled
}

fun ViewGroup.deepForEach(function: View.() -> Unit) {
    this.forEach { child ->
        child.function()
        if (child is ViewGroup) {
            child.deepForEach(function)
        }
    }
}