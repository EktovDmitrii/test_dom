package com.custom.rgs_android_dom.ui.registration.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentDemoBinding
import com.custom.rgs_android_dom.databinding.FragmentRegistrationPhoneBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import kotlinx.android.synthetic.main.fragment_registration_phone.*


class RegistrationPhoneFragment : BaseFragment<RegistrationPhoneViewModel, FragmentRegistrationPhoneBinding>(RegistrationPhoneViewModel::class.java) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.demoTextObserver.observe(viewLifecycleOwner) {
            //binding.textDemoSimple.text = it
        }
        binding.phoneInput.setOnCountryClickListener {
            viewModel.onCountryClick(it)
        }
        nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentRegistrationPhoneBinding {
        return FragmentRegistrationPhoneBinding.inflate(inflater, container, false)
    }
}