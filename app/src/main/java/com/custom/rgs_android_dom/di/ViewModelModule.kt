package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.countries.CountriesViewModel
import com.custom.rgs_android_dom.ui.demo.DemoViewModel
import com.custom.rgs_android_dom.ui.main.MainViewModel
import com.custom.rgs_android_dom.ui.client.ClientViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.edit.EditPersonalDataViewModel
import com.custom.rgs_android_dom.ui.registration.code.RegistrationCodeViewModel
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementViewModel
import com.custom.rgs_android_dom.ui.registration.fill_client.RegistrationFillClientViewModel
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneViewModel
import com.custom.rgs_android_dom.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { parameters -> RegistrationCodeViewModel(phone = parameters[0], token = parameters[1], registrationInteractor = get()) }
    viewModel { RegistrationPhoneViewModel(countriesInteractor = get(), registrationInteractor = get()) }
    viewModel { SplashViewModel(registrationInteractor = get()) }
    viewModel { DemoViewModel() }
    viewModel { parameters-> RegistrationAgreementViewModel(phone = parameters.get(), registrationInteractor = get()) }
    viewModel { parameters-> RegistrationFillClientViewModel(phone = parameters.get(), clientInteractor= get()) }
    viewModel { parameters-> CountriesViewModel(selectedCountryLetterCode = parameters.get(), countriesInteractor = get())}
    viewModel { ClientViewModel(clientInteractor = get(), registrationInteractor = get()) }
    viewModel { MainViewModel(registrationInteractor = get()) }
    viewModel { EditPersonalDataViewModel(clientInteractor = get()) }
}