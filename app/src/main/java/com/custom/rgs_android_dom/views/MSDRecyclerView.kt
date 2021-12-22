package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.utils.recycler_view.HorizontalItemDecoration
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration

class MSDRecyclerView(context: Context, attributeSet: AttributeSet) : RecyclerView(context, attributeSet) {

    init {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.MSDRecyclerView, 0, 0)

        val horizontalGap = attrs.getDimension(R.styleable.MSDRecyclerView_horizontalDecorationGap, -1f)
        val verticalGap = attrs.getDimension(R.styleable.MSDRecyclerView_verticalDecorationGap, -1f)

        if (horizontalGap != -1f){
            this.addItemDecoration(HorizontalItemDecoration(gap = horizontalGap.toInt()))
        }

        if (verticalGap != -1f){
            this.addItemDecoration(VerticalItemDecoration(gap = verticalGap.toInt()))
        }
    }


}
