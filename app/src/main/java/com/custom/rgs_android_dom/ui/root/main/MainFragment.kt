package com.custom.rgs_android_dom.ui.root.main

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentMainBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*

class MainFragment : BaseBottomSheetFragment<MainViewModel, FragmentMainBinding>() {

    override val TAG: String = "MAIN_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInLinearLayout.setOnDebouncedClickListener {
            viewModel.onSignInClick()
        }

        binding.profileLinearLayout.setOnDebouncedClickListener {
            viewModel.onProfileClick()
        }

        subscribe(viewModel.registrationObserver) {
            binding.profileLinearLayout.visibleIf(it)
            binding.signInLinearLayout.goneIf(it)
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheetNoDim
    }
}