package com.custom.rgs_android_dom.ui.registration.phone

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationPhoneBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.hideSoftwareKeyboard
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

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

            binding.phoneInput.setOnDoneClickListener {
                viewModel.onDoneClick()
            }
        }

        subscribe(viewModel.closeObserver) {
            ScreenManager.back(getNavigateId())
        }

        subscribe(viewModel.loadingStateObserver){
            handleState(it)
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


    private fun handleState(state: BaseViewModel.LoadingState){
        when (state){
            BaseViewModel.LoadingState.CONTENT -> {
                binding.nextTextView.setLoading(false)
            }
            BaseViewModel.LoadingState.LOADING -> {
                binding.nextTextView.setLoading(true)
            }
        }
    }
}