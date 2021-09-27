package com.custom.rgs_android_dom.views

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager


class MSDSizerPager(context: Context, attributeSet: AttributeSet) : ViewPager(
    context,
    attributeSet
) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var customHeightMeasureSpec = heightMeasureSpec
        var height = 0
        val childWidthSpec = MeasureSpec.makeMeasureSpec(
            Math.max(
                0, MeasureSpec.getSize(widthMeasureSpec) -
                        paddingLeft - paddingRight
            ),
            MeasureSpec.getMode(widthMeasureSpec)
        )
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(childWidthSpec, MeasureSpec.UNSPECIFIED)
            val h = child.measuredHeight
            if (h > height) height = h
        }

        if (height != 0) {
            customHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, customHeightMeasureSpec)
    }

}
