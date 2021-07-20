package com.custom.rgs_android_dom.ui.registration.code

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.custom.rgs_android_dom.databinding.FragmentRegistrationCodeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationCodeFragment : BaseFragment<RegistrationCodeViewModel, FragmentRegistrationCodeBinding>() {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"
        private const val TIMER_MILLIS: Long = 60000

        fun newInstance(phone: String): RegistrationCodeFragment {
            return RegistrationCodeFragment().args {
                putString(ARG_PHONE, phone)
            }
        }
    }

    private var timer: CountDownTimer? = null

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

        subscribe(viewModel.phoneObserver){phone->
            binding.phoneTextView.text = "Мы отправили СМС на номер $phone"
        }

        subscribe(viewModel.closeObserver){
            stopTimer()
            hideSoftwareKeyboard()
            ScreenManager.back(getNavigateId())
        }

        subscribe(viewModel.startTimerObserver){
            startCountdownTimer()
        }

        subscribe(viewModel.showResendCodeObserver){
            stopTimer()
            binding.countdownTextView.gone()
            binding.resendCodeTextView.visible()
        }

        subscribe(viewModel.loadingStateObserver){
            handleState(it)
        }

        subscribe(viewModel.errorMessageObserver){
            requireActivity().toast(it)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater,container: ViewGroup?): FragmentRegistrationCodeBinding {
        return FragmentRegistrationCodeBinding.inflate(inflater, container, false)
    }

    private fun stopTimer(){
        timer?.cancel()
    }

    private fun startCountdownTimer(){
        binding.countdownTextView.visible()
        binding.resendCodeTextView.gone()
        binding.codeInput.reset()
        stopTimer()
        timer = object : CountDownTimer(TIMER_MILLIS, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val secondsLeft = String.format("%02d", millisUntilFinished.div(1000))
                binding.countdownTextView.text = "Вы сможете повторно запросить код через 00:$secondsLeft"
            }

            override fun onFinish() {
                viewModel.onTimerFinished()
            }
        }
        timer?.start()
    }

    private fun handleState(state: BaseViewModel.LoadingState){
        when (state){
            BaseViewModel.LoadingState.LOADING -> {
                binding.codeInput.isEnabled = false
                binding.countdownTextView.isEnabled = false
                binding.phoneTextView.isEnabled = false
                binding.resendCodeTextView.isEnabled = false

                binding.nextTextView.setLoading(true)
            }
            BaseViewModel.LoadingState.CONTENT -> {
                binding.codeInput.isEnabled = true
                binding.countdownTextView.isEnabled = true
                binding.phoneTextView.isEnabled = true
                binding.resendCodeTextView.isEnabled = true

                binding.nextTextView.setLoading(false)
            }
            BaseViewModel.LoadingState.ERROR -> {
                requireContext().vibratePhone()
                binding.codeInput.isEnabled = true
                binding.codeInput.setErrorState()
                binding.countdownTextView.isEnabled = true
                binding.phoneTextView.isEnabled = true
                binding.resendCodeTextView.isEnabled = true

                binding.nextTextView.setLoading(false)
            }
        }
    }
}