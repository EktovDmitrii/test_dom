package com.custom.rgs_android_dom.utils.recycler_view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            outRect.left = 0
            outRect.right = space / 2
        } else {
            outRect.left = space / 2
            outRect.right = 0
        }

        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = 0
        } else {
            outRect.top = space / 2
        }

        outRect.bottom = space / 2
    }
}