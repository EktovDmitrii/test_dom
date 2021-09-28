package com.custom.rgs_android_dom.ui.screen_stub

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentScreenStubBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

class ScreenStubFragment : BaseFragment<ScreenStubViewModel, FragmentScreenStubBinding>(R.layout.fragment_screen_stub) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }
    }

}