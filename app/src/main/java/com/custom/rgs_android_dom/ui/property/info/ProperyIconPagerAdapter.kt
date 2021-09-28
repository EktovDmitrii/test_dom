package com.custom.rgs_android_dom.ui.property.info

import android.content.Context
import android.view.View
import androidx.viewpager.widget.PagerAdapter

class ProperyIconPagerAdapter(
    private val context: Context?
): PagerAdapter() {

    private val data = mutableListOf<ProperyInfoItem>()

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean =
        view == obj

    fun setData(items : List<ProperyInfoItem>){
        this.data.clear()
        this.data.addAll(items)
        notifyDataSetChanged()
    }
}