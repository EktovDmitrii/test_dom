package com.custom.rgs_android_dom.ui.chat

import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.utils.dpToPx


class ChatAnimator(
    private val animatingView: View
) : RecyclerView.OnScrollListener() {

    private val animDuration = 300L
    private var hideAnim: ObjectAnimator? = null
    private var showAnim: ObjectAnimator? = null
    private var isShown = false


    private fun showToLastChatItemButton() {
        isShown = true
        showAnim?.start()
    }

    private fun hideToLastChatItemButton() {
        isShown = false
        hideAnim?.start()
    }

    private fun prepareAnimations() {

        hideAnim = ObjectAnimator.ofFloat(
            animatingView,
            "translationY",
            0f
        ).apply {
            this.duration = animDuration
        }

        showAnim = ObjectAnimator.ofFloat(
            animatingView,
            "translationY",
            -(animatingView.height + 12.dpToPx(animatingView.context)).toFloat()
        ).apply {
            this.duration = animDuration
        }

    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        prepareAnimations()

        if (dy > 0 && !isShown) {
            showToLastChatItemButton()
        } else if ((dy < 0 && isShown)) {
            hideToLastChatItemButton()
        } else if (!recyclerView.canScrollVertically(1) && isShown) {
            hideToLastChatItemButton()
        }

    }

}