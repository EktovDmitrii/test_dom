package com.custom.rgs_android_dom.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSplashBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.demo.DemoFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager

//ToDo: запретить закрытие
class SplashFragment() :
    BaseFragment<SplashViewModel, FragmentSplashBinding>(SplashViewModel::class.java) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingStateObserver.observe(viewLifecycleOwner) {
            when (it!!) {
                BaseViewModel.LoadingState.LOADING -> { }
                BaseViewModel.LoadingState.CONTENT -> {
                    close()
                }
                BaseViewModel.LoadingState.ERROR -> { }
            }
        }
    }

    private fun close() {
        ScreenManager.back(this.getNavigateId())
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }
}