package com.custom.rgs_android_dom.ui.registration.code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationCodeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationCodeFragment : BaseFragment<RegistrationCodeViewModel, FragmentRegistrationCodeBinding>(
    R.layout.fragment_registration_code) {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"

        fun newInstance(phone: String): RegistrationCodeFragment {
            return RegistrationCodeFragment().args {
                putString(ARG_PHONE, phone)
            }
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(ARG_PHONE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.codeInput.setOnCodeCompleteListener {
            viewModel.onCodeComplete(it)
        }

        binding.resendCodeTextView.setOnDebouncedClickListener {
            viewModel.onResendCodeClick()
        }

        subscribe(viewModel.phoneObserver){
            binding.phoneTextView.text = it
        }

        subscribe(viewModel.countdownTextObserver){
            binding.countdownTextView.text = it
        }

        subscribe(viewModel.onTimerStartObserver){
            binding.countdownTextView.visible()
            binding.resendCodeTextView.gone()
            binding.codeInput.reset()
        }

        subscribe(viewModel.showResendCodeObserver){
            binding.countdownTextView.gone()
            binding.resendCodeTextView.visible()
        }

        subscribe(viewModel.codeErrorObserver){
            onCodeError()
        }

    }

    override fun onLoading() {
        super.onLoading()
        binding.codeInput.isEnabled = false
        binding.countdownTextView.isEnabled = false
        binding.phoneTextView.isEnabled = false
        binding.resendCodeTextView.isEnabled = false

        binding.nextTextView.setLoading(true)
    }

    override fun onContent() {
        super.onContent()
        binding.codeInput.isEnabled = true
        binding.countdownTextView.isEnabled = true
        binding.phoneTextView.isEnabled = true
        binding.resendCodeTextView.isEnabled = true

        binding.nextTextView.setLoading(false)
    }

    override fun onError() {
        super.onError()
        binding.codeInput.isEnabled = true
        binding.countdownTextView.isEnabled = true
        binding.phoneTextView.isEnabled = true
        binding.resendCodeTextView.isEnabled = true

        binding.nextTextView.setLoading(false)

        toast("Произошла ошибка")
    }

    override fun onClose() {
        //super.onClose()
        hideSoftwareKeyboard()
        ScreenManager.closeScope(REGISTRATION)
    }

    private fun onCodeError() {
        requireContext().vibratePhone()
        binding.codeInput.isEnabled = true
        binding.codeInput.setErrorState()
        binding.countdownTextView.isEnabled = true
        binding.phoneTextView.isEnabled = true
        binding.resendCodeTextView.isEnabled = true

        binding.nextTextView.setLoading(false)
    }
}