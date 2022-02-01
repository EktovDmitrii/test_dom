package com.custom.rgs_android_dom.ui.stories.tabs

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabSupportBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.stories.TabActionListener
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabSupportFragment() :
    BaseFragment<TabSupportViewModel, FragmentTabSupportBinding>(R.layout.fragment_tab_support) {

    private var tabActionListener: TabActionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabActionListener = parentFragment as TabActionListener

        binding.supportLeftFrameLayout.setOnDebouncedClickListener { tabActionListener?.onLeftClick() }

        binding.understandFrameLayout.setOnDebouncedClickListener { tabActionListener?.onUnderstandClick() }

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

}
