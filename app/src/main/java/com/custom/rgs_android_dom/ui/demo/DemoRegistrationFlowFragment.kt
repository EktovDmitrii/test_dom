package com.custom.rgs_android_dom.ui.demo

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDemoBinding
import com.custom.rgs_android_dom.databinding.FragmentDemoRegistrationFlowBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener

class DemoRegistrationFlowFragment: BaseFragment<DemoViewModel, FragmentDemoRegistrationFlowBinding>(R.layout.fragment_demo_registration_flow) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationButton.setOnDebouncedClickListener {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

}