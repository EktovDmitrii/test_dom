package com.custom.rgs_android_dom.ui.splash

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSplashBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.activity.hideKeyboardForced
import com.custom.rgs_android_dom.utils.setStatusBarColor

//ToDo: запретить закрытие
class SplashFragment() : BaseFragment<SplashViewModel, FragmentSplashBinding>(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().hideKeyboardForced()
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.secondary100)
    }
}