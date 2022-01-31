package com.custom.rgs_android_dom.ui.stories.tabs

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentTabNewServiceBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class TabNewServiceFragment(private val onRightClick: () -> Unit) :
    BaseFragment<TabNewServiceViewModel, FragmentTabNewServiceBinding>(R.layout.fragment_tab_new_service) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newServiceRightFrameLayout.setOnDebouncedClickListener(onRightClick)

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

}
