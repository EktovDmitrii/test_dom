package com.custom.rgs_android_dom.ui.registration.agreement

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationAgreementBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.ui.web_view.WebViewFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class RegistrationAgreementFragment :
    BaseFragment<RegistrationAgreementViewModel, FragmentRegistrationAgreementBinding>(
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

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_PHONE)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeAgreementLinks()

        binding.acceptAgreementCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onAcceptAgreementCheckedChanged(isChecked)
        }

        binding.nextTextView.setOnDebouncedClickListener {
            viewModel.onNextClick()
        }

        binding.backImageView.setOnDebouncedClickListener {
            onClose()
        }

        subscribe(viewModel.isNextTextViewEnabledObserver) {
            binding.nextTextView.isEnabled = it
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }
    }

    override fun onClose() {
        viewModel.onBackClick { isAccepted ->
            hideSoftwareKeyboard()
            viewModel.disposeAll()
            if (isAccepted) {
                ScreenManager.closeScope(REGISTRATION)
            } else {
                super.onClose()
                ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
            }
        }
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

    private fun makeAgreementLinks() {
        binding.agreementTextView.makeStringWithLink(
            resources.getColor(R.color.primary500,null),
            Pair("пользовательского соглашения,", View.OnClickListener {
                val webViewFragment =
                    WebViewFragment.newInstance("https://moi-service.ru/legal/moi-service-dom/polzovatelskoe-soglashenie")
                ScreenManager.showScreen(webViewFragment)
            }),
            Pair("политику обработки персональных данных", View.OnClickListener {
                val webViewFragment =
                    WebViewFragment.newInstance("https://moi-service.ru/legal/policy")
                ScreenManager.showScreen(webViewFragment)
            }),
            Pair("согласие на обработку персональных данных", View.OnClickListener {
                val webViewFragment =
                    WebViewFragment.newInstance("https://moi-service.ru/legal/soglasie-polzovatelya-na-obrabotku-personalnyh-dannyh")
                ScreenManager.showScreen(webViewFragment)
            })

        )
    }

}