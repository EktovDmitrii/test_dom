package com.custom.rgs_android_dom.ui.property.info

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentPropertyBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class PropertyInfoFragment(): BaseBottomSheetFragment<PropertyInfoViewModel, FragmentPropertyBinding>() {
    override val TAG: String = "PROPERTY_INFO_FRAGMENT"

    override fun getSwipeAnchor(): View? {
        return binding.swipeAnchorLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backImageView.setOnDebouncedClickListener {
            ScreenManager.closeCurrentBottomFragment()
        }
    }
}