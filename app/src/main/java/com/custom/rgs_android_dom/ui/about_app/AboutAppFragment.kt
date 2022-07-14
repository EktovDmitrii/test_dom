package com.custom.rgs_android_dom.ui.about_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAboutAppBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.constants.LEGAL_POLICY_LINK
import com.custom.rgs_android_dom.ui.constants.PLAY_GOOGLE_LINK
import com.custom.rgs_android_dom.ui.constants.USER_AGREEMENT_LINK
import com.custom.rgs_android_dom.ui.constants.USER_SUPPORT_LINK
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.web_view.WebViewFragment
import com.custom.rgs_android_dom.utils.*

class AboutAppFragment : BaseFragment<AboutAppViewModel, FragmentAboutAppBinding>(R.layout.fragment_about_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.rateLinearLayout.setOnDebouncedClickListener {
            try {
                val appStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=ru.moi_service.property")).apply {
                    setPackage("com.android.vending")
                }
                startActivity(appStoreIntent)
            } catch (exception: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_GOOGLE_LINK)))
            }
        }

        binding.licenseAgreementsLinearLayout.setOnDebouncedClickListener {
            ScreenManager.showScreen(WebViewFragment.newInstance(USER_AGREEMENT_LINK))
        }

        binding.privacyPolicyLinearLayout.setOnDebouncedClickListener {
            ScreenManager.showScreen(WebViewFragment.newInstance(LEGAL_POLICY_LINK))
        }

        binding.feedbackLinearLayout.setOnDebouncedClickListener {
            ScreenManager.showScreen(WebViewFragment.newInstance(USER_SUPPORT_LINK))
        }

        binding.versionTextView.text = TranslationInteractor.getTranslation("app.about_app.footer.version").replace("%@", BuildConfig.VERSION_NAME)
    }
}
