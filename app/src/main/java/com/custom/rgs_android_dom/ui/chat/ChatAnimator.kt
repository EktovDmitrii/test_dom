package com.custom.rgs_android_dom.ui.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ChatAnimator(private val animatingView: View) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0 && !animatingView.isActivated) {
            animatingView.isActivated = true
        } else if ((dy < 0 && animatingView.isActivated)) {
            animatingView.isActivated = false
        } else if (!recyclerView.canScrollVertically(1) && animatingView.isActivated) {
            animatingView.isActivated = false
        }

    }

}
