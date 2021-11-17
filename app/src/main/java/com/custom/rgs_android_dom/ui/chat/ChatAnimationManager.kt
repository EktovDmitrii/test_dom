package com.custom.rgs_android_dom.ui.chat

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Interpolator
import androidx.recyclerview.widget.RecyclerView


class ChatAnimationManager(
    context: Context?,
    animatingView: View
) : RecyclerView.OnScrollListener() {

    private var hideAnim: ObjectAnimator? = null
    private var showAnim: ObjectAnimator? = null
    var isShown = false

    init {
        val translationY = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            108F,
            context?.resources?.displayMetrics
        )
        hideAnim = ObjectAnimator.ofFloat(animatingView, "translationY", 0f).apply {
            this.duration = 500
        }
        showAnim = ObjectAnimator.ofFloat(animatingView, "translationY", -translationY).apply {
            this.duration = 500
        }

    }

    private fun showToLastChatItemButton() {
        isShown = true
        showAnim?.start()
    }

    private fun hideToLastChatItemButton() {
        isShown = false
        hideAnim?.start()
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0 && !isShown) {
            showToLastChatItemButton()
        } else if ((dy < 0 && isShown)) {
            hideToLastChatItemButton()
        } else if (!recyclerView.canScrollVertically(1) && isShown) {
            hideToLastChatItemButton()
        }
    }
}