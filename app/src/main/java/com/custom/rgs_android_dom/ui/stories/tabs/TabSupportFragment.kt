package com.custom.rgs_android_dom.ui.stories.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabSupportBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabSupportFragment(
    private val onLeftClick: () -> Unit,
    private val onUnderstandClick: () -> Unit
) : BaseFragment<TabSupportViewModel, FragmentTabSupportBinding>(R.layout.fragment_tab_support) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.supportLeftFrameLayout.setOnDebouncedClickListener(onLeftClick)

        binding.understandFrameLayout.setOnDebouncedClickListener(onUnderstandClick)

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

}
