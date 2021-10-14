package com.custom.rgs_android_dom.ui.registration.phone

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationPhoneBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.MSDPhoneInput

class RegistrationPhoneFragment : BaseFragment<RegistrationPhoneViewModel, FragmentRegistrationPhoneBinding>(
    R.layout.fragment_registration_phone
) {

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

            binding.phoneInput.setLetterCode(it.letterCode)

            binding.phoneInput.setOnDoneClickListener {
                viewModel.onDoneClick()
            }
        }

        binding.phoneInput.setOnCountryClickListener {
            viewModel.onCountryClick(it)
        }

        subscribe(viewModel.phoneErrorObserver){error->
            if (error.isNotEmpty()){
                binding.phoneInput.setState(MSDPhoneInput.State.ERROR, error)
            } else {
                binding.phoneInput.setState(MSDPhoneInput.State.NORMAL)
            }
        }

        binding.phoneInput.setOnCountryClickListener {
            viewModel.onCountryClick(it)
        }

        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
            hideSoftwareKeyboard()
        }

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }
    }

    override fun onContent() {
        super.onContent()
        binding.nextTextView.setLoading(false)
    }

    override fun onLoading() {
        super.onLoading()
        binding.nextTextView.setLoading(true)
    }

    override fun onError() {
        super.onError()
        binding.nextTextView.setLoading(false)
    }
}