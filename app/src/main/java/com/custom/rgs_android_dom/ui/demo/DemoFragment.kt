package com.custom.rgs_android_dom.ui.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentDemoBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment


class DemoFragment() : BaseFragment<DemoViewModel, FragmentDemoBinding>(DemoViewModel::class.java) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.demoTextObserver.observe(viewLifecycleOwner) {
            binding.textDemoSimple.text = it
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDemoBinding {
        return FragmentDemoBinding.inflate(inflater, container, false)
    }
}