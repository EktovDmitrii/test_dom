package com.custom.rgs_android_dom.utils.recycler_view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridThreeSpanItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var position = parent.getChildLayoutPosition(view) + 1
        when {
            position % 3 == 0 -> {
                outRect.right = 0
                outRect.left = space / 2
            }
            (position - 1) % 3 == 0 -> {
                outRect.left = 0
                outRect.right = space / 2
            }
            (position + 1) % 3 == 0 -> {
                outRect.right = space / 2
                outRect.left = space / 2
            }
        }

        if (position in 1..3) {
            outRect.top = 0
        } else {
            outRect.top = space
        }

    }
}