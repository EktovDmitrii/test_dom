package com.custom.rgs_android_dom.utils.recycler_view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    private val startGap: Int = 0,
    private val endGap: Int = 0,
    private val gap: Int = 0,
    private val topGap: Int = 0,
    private val bottomGap: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        outRect.top = topGap
        outRect.bottom = bottomGap

        val last = parent.adapter?.itemCount?.let { it - 1 }
        if (last == 0) {
            outRect.left = startGap
            outRect.right = endGap
        }

        when (position) {
            0 -> outRect.left = startGap
            last -> {
                outRect.right = endGap
                outRect.left = gap
            }
            else -> outRect.left = gap
        }


    }
}