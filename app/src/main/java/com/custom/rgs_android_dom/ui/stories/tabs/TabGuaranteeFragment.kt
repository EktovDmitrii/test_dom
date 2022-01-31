package com.custom.rgs_android_dom.ui.stories.tabs

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabGuaranteeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabGuaranteeFragment(
    private val onRightClick: () -> Unit,
    private val onLeftClick: () -> Unit
) :
    BaseFragment<TabGuaranteeViewModel, FragmentTabGuaranteeBinding>(R.layout.fragment_tab_guarantee) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.guaranteeLeftFrameLayout.setOnDebouncedClickListener(onLeftClick)

        binding.guaranteeRightFrameLayout.setOnDebouncedClickListener(onRightClick)

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

}
