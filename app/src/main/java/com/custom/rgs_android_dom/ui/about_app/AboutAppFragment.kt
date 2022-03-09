package com.custom.rgs_android_dom.ui.about_app

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAboutAppBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.web_view.WebViewFragment
import com.custom.rgs_android_dom.utils.*

class AboutAppFragment : BaseFragment<AboutAppViewModel, FragmentAboutAppBinding>(R.layout.fragment_about_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.licenseAgreementsLinearLayout.setOnDebouncedClickListener {
            ScreenManager.showScreen(WebViewFragment.newInstance("https://moi-service.ru/legal/moi-service-dom/polzovatelskoe-soglashenie"))
        }

        binding.privacyPolicyLinearLayout.setOnDebouncedClickListener {
            ScreenManager.showScreen(WebViewFragment.newInstance("https://moi-service.ru/legal/policy"))
        }


        binding.versionTextView.text = "Версия ${BuildConfig.VERSION_NAME}"

    }

}