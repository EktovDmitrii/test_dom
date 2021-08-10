package com.custom.rgs_android_dom.ui.registration.code

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationCodeBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.activity_contracts.OTCActivityContract
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationCodeFragment : BaseFragment<RegistrationCodeViewModel, FragmentRegistrationCodeBinding>(
    R.layout.fragment_registration_code) {

    companion object {
        private const val ARG_PHONE = "ARG_PHONE"
        private const val ARG_TOKEN = "ARG_TOKEN"

        fun newInstance(phone: String, token: String): RegistrationCodeFragment {
            return RegistrationCodeFragment().args {
                putString(ARG_PHONE, phone)
                putString(ARG_TOKEN, token)
                Log.d("MyLog", "TOKEN " + token)
            }
        }
    }

    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            getOTCAction.launch(consentIntent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                    }
                }
            }
        }
    }

    private val getOTCAction = registerForActivityResult(OTCActivityContract()){ otc->
        viewModel.onOTCReceived(otc)
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PHONE),
            requireArguments().getString(ARG_TOKEN)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeImageView.setOnDebouncedClickListener {
            viewModel.onCloseClick()
        }

        binding.backImageView.setOnDebouncedClickListener {
            ScreenManager.back(getNavigateId())
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
            startSMSReceiverTask()
        }

        subscribe(viewModel.showResendCodeObserver){
            binding.countdownTextView.gone()
            binding.resendCodeTextView.visible()
        }

        subscribe(viewModel.codeErrorObserver){
            onCodeError(it)
        }

        subscribe(viewModel.otcReceivedObserver){otc->
            binding.codeInput.code = otc
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
        hideSoftwareKeyboard()
        ScreenManager.closeScope(REGISTRATION)
    }

    private fun onCodeError(error: String) {
        requireContext().vibratePhone()
        binding.codeInput.isEnabled = true
        binding.codeInput.setErrorState(error)
        binding.countdownTextView.isEnabled = true
        binding.phoneTextView.isEnabled = true
        binding.resendCodeTextView.isEnabled = true

        binding.nextTextView.setLoading(false)
    }

    override fun onStart() {
        super.onStart()
        registerSMSVerificationReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterSMSVerificationReceiver()
    }

    private fun registerSMSVerificationReceiver(){
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireContext().registerReceiver(smsVerificationReceiver,  intentFilter, SmsRetriever.SEND_PERMISSION, null)
    }

    private fun unregisterSMSVerificationReceiver(){
        requireContext().unregisterReceiver(smsVerificationReceiver)
    }

    private fun startSMSReceiverTask(){
        SmsRetriever.getClient(requireContext()).startSmsUserConsent(null)
    }
}