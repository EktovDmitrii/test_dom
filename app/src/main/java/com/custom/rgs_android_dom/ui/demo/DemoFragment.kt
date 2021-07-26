package com.custom.rgs_android_dom.ui.demo

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDemoBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

class DemoFragment(val scopeId: Int? = null) : BaseFragment<DemoViewModel, FragmentDemoBinding>(R.layout.fragment_demo) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.demoTextObserver.observe(viewLifecycleOwner) {
            binding.textDemoSimple.text = this.getNavigateId().toString()
        }

        binding.closeScoupeButton.setOnClickListener {
            scopeId?.let { ScreenManager.closeScope(it) }
        }

        binding.newScreenButton.setOnClickListener {
            ScreenManager.showScreen(DemoFragment())
        }

        binding.newScreenScoupeButton.setOnClickListener {
            ScreenManager.showScreenScope(DemoFragment(1), 1)
        }

        binding.prevButton.setOnClickListener {
            ScreenManager.back(getNavigateId())
        }
    }

}