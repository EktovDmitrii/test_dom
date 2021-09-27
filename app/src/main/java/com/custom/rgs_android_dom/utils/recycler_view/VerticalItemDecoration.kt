package com.custom.rgs_android_dom.utils.recycler_view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration(
    private val topGap: Int = 0,
    private val bottomGap: Int = 0,
    private val gap: Int = 0,
    private val leftGap: Int = 0,
    private val rightGap: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        when (position) {
            0 -> {
                outRect.top = topGap
                if (parent.adapter?.itemCount == 1) outRect.bottom = bottomGap
            }
            parent.adapter?.itemCount?.let { it - 1 } -> {
                outRect.top = gap
                outRect.bottom = bottomGap
            }
            else -> outRect.top = gap
        }

        outRect.left = leftGap
        outRect.right = rightGap
    }
}