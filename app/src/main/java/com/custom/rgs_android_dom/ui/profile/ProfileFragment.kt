package com.custom.rgs_android_dom.ui.profile

import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentProfileBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment

class ProfileFragment(
    peekHeight: Int = 0,
    topMargin: Int = 0
) : BaseBottomSheetFragment<ProfileViewModel, FragmentProfileBinding>(peekHeight, topMargin) {

    override val TAG: String = "PROFILE_FRAGMENT"

    override fun getSwipeAnchor(): View? {
        return binding.swipeAnchorLayout.root
    }


}