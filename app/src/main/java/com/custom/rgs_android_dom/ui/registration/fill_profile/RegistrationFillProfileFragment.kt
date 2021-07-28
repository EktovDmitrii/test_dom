package com.custom.rgs_android_dom.ui.registration.fill_profile

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRegistrationAgreementBinding
import com.custom.rgs_android_dom.databinding.FragmentRegistrationFillProfileBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.*

class RegistrationFillProfileFragment : BaseFragment<RegistrationFillProfileViewModel, FragmentRegistrationFillProfileBinding>(
    R.layout.fragment_registration_fill_profile
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onClose() {
        hideSoftwareKeyboard()
        ScreenManager.closeScope(REGISTRATION)
    }

    override fun onLoading() {
        super.onLoading()
    }

    override fun onError() {
        super.onError()
    }

    override fun onContent() {
        super.onContent()
    }

}