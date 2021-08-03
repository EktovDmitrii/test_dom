package com.custom.rgs_android_dom.ui.registration.agreement

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationAgreementBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationAgreementFragment : BaseFragment<RegistrationAgreementViewModel, FragmentRegistrationAgreementBinding>(
    R.layout.fragment_registration_agreement
) {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"

        fun newInstance(phone: String): RegistrationAgreementFragment {
            return RegistrationAgreementFragment().args {
                putString(ARG_PHONE, phone)
            }
        }
    }

    override fun getParameters(): ParametersDefinition  = {
        parametersOf(requireArguments().getString(ARG_PHONE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.acceptAgreementCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAcceptAgreementCheckedChanged(isChecked)
        }

        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
        }

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        subscribe(viewModel.isNextTextViewEnabledObserver){
            binding.nextTextView.isEnabled = it
        }

        subscribe(viewModel.legalTextObserver){
            binding.agreementTextView.text = it
            binding.agreementTextView.stripUnderlines()
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        ScreenManager.closeScope(REGISTRATION)
    }

    override fun onLoading() {
        super.onLoading()
        binding.nextTextView.setLoading(true)
    }

    override fun onError() {
        super.onError()
        binding.nextTextView.setLoading(false)
    }

    override fun onContent() {
        super.onContent()
        binding.nextTextView.setLoading(false)
    }

}