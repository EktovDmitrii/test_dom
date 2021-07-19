package com.custom.rgs_android_dom.ui.registration.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentRegistrationPhoneBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class RegistrationPhoneFragment : BaseFragment<RegistrationPhoneViewModel, FragmentRegistrationPhoneBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe(viewModel.isNextTextViewEnabledObserver){
            binding.nextTextView.isEnabled = it
        }
        subscribe(viewModel.countryObserver){
            binding.phoneInput.setCountryImage(it.image)

            binding.phoneInput.setMask(it.mask){phone, isMaskFilled ->
                viewModel.onPhoneChanged(phone, isMaskFilled)
            }
        }

        binding.phoneInput.setOnCountryClickListener {
            viewModel.onCountryClick(it)
        }
        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
        }
        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentRegistrationPhoneBinding {
        return FragmentRegistrationPhoneBinding.inflate(inflater, container, false)
    }
}