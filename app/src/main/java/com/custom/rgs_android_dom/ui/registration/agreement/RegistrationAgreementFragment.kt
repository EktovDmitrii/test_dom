package com.custom.rgs_android_dom.ui.registration.agreement

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationAgreementBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.constants.LEGAL_POLICY_LINK
import com.custom.rgs_android_dom.ui.constants.PERSONAL_DATE_LINK
import com.custom.rgs_android_dom.ui.constants.USER_AGREEMENT_LINK
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.web_view.WebViewFragment
import com.custom.rgs_android_dom.utils.*
import com.yandex.metrica.YandexMetrica
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
            YandexMetrica.reportEvent("login_step_3_reg")

            viewModel.onNextClick()
        }

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        subscribe(viewModel.isNextTextViewEnabledObserver) {
            binding.nextTextView.isEnabled = it
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }
        subscribe(viewModel.isSignedBeforeCloseObserver) {
            if (it) {
                ScreenManager.closeScope(REGISTRATION)
            } else {
                onClose()
            }
        }
    }

    override fun onClose() {
        if (viewModel.isSignedBeforeCloseObserver.value == null) {
            viewModel.onBackClick()
        } else {
            super.onClose()
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
        val agreement: String = TranslationInteractor.getTranslation("app.registration.agreement.first_text") +
                TranslationInteractor.getTranslation("app.registration.agreement.first_link") +
                TranslationInteractor.getTranslation("app.registration.agreement.second_link") +
                TranslationInteractor.getTranslation("app.registration.agreement.second_text") +
                TranslationInteractor.getTranslation("app.registration.agreement.third_link")
       binding.agreementTextView.text = agreement

        binding.agreementTextView.makeStringWithLink(
            resources.getColor(R.color.primary500, null),
            Pair(TranslationInteractor.getTranslation("app.registration.agreement.first_link"), View.OnClickListener {
                val webViewFragment =
                    WebViewFragment.newInstance(USER_AGREEMENT_LINK)
                ScreenManager.showScreen(webViewFragment)
            }),
            Pair(TranslationInteractor.getTranslation("app.registration.agreement.second_link"), View.OnClickListener {
                val webViewFragment =
                    WebViewFragment.newInstance(LEGAL_POLICY_LINK)
                ScreenManager.showScreen(webViewFragment)
            }),
            Pair(TranslationInteractor.getTranslation("app.registration.agreement.third_link"), View.OnClickListener {
                val webViewFragment =
                    WebViewFragment.newInstance(PERSONAL_DATE_LINK)
                ScreenManager.showScreen(webViewFragment)
            })
        )
    }
}
