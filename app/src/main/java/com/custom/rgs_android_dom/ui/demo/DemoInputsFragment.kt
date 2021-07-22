package com.custom.rgs_android_dom.ui.demo

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentDemoBinding
import com.custom.rgs_android_dom.databinding.FragmentDemoInputsBinding
import com.custom.rgs_android_dom.databinding.FragmentDemoRegistrationFlowBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.views.edit_text.MSDEditText

class DemoInputsFragment: BaseFragment<DemoViewModel, FragmentDemoInputsBinding>(R.layout.fragment_demo_inputs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.labelTextView.setValue("Иванов Иван Русланович")

        binding.labelIconTextView.setIcon(R.drawable.ic_mastercard)
        binding.labelIconTextView.setValue("•••• 3084")

        binding.firstEt.setState(MSDEditText.State.SUCCESS)
        binding.secondEt.setState(MSDEditText.State.ERROR)
        binding.thirdEt.setState(MSDEditText.State.DISABLED)

    }

}