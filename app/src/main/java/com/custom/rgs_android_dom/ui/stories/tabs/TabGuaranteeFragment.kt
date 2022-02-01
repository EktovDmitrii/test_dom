package com.custom.rgs_android_dom.ui.stories.tabs

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabGuaranteeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.stories.TabActionListener
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabGuaranteeFragment() :
    BaseFragment<TabGuaranteeViewModel, FragmentTabGuaranteeBinding>(R.layout.fragment_tab_guarantee) {

    private var tabActionListener: TabActionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabActionListener = parentFragment as TabActionListener

        binding.guaranteeLeftFrameLayout.setOnDebouncedClickListener { tabActionListener?.onLeftClick() }

        binding.guaranteeRightFrameLayout.setOnDebouncedClickListener { tabActionListener?.onRightClick() }

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

}
