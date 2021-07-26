package com.custom.rgs_android_dom.ui.demo

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDemoBinding
import com.custom.rgs_android_dom.databinding.FragmentDemoButtonsBinding
import com.custom.rgs_android_dom.databinding.FragmentDemoRegistrationFlowBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class DemoButtonsFragment: BaseFragment<DemoViewModel, FragmentDemoButtonsBinding>(R.layout.fragment_demo_buttons) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.firstTv.setLoading(true)
        binding.secondTv.setLoading(true)
    }

}