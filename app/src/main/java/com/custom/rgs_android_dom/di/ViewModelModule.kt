package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.demo.DemoViewModel
import com.custom.rgs_android_dom.ui.registration.code.RegistrationCodeViewModel
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementViewModel
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneViewModel
import com.custom.rgs_android_dom.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { parameters -> RegistrationCodeViewModel(phone = parameters.get(), registrationInteractor = get()) }
    viewModel { RegistrationPhoneViewModel(countriesInteractor = get(), registrationInteractor = get()) }
    viewModel { SplashViewModel() }
    viewModel { DemoViewModel() }
    viewModel { RegistrationAgreementViewModel(registrationInteractor = get()) }
}