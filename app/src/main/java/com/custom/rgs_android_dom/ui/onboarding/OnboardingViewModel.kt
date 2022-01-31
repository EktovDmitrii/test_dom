package com.custom.rgs_android_dom.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.ui.root.RootFragment

class OnboardingViewModel : BaseViewModel() {

    private val isClosableController = MutableLiveData(false)
    val isClosableObserver: LiveData<Boolean> = isClosableController

    fun onSkipClick() {
        ScreenManager.showScreen(RootFragment())
        isClosableController.value = true
        closeController.value = Unit
    }

    fun onLoginClick() {
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        isClosableController.value = true
        closeController.value = Unit
    }
}