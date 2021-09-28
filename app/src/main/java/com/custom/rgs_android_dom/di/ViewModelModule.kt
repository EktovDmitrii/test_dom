package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.ui.about_app.AboutAppViewModel
import com.custom.rgs_android_dom.ui.chat.ChatViewModel
import com.custom.rgs_android_dom.ui.countries.CountriesViewModel
import com.custom.rgs_android_dom.ui.demo.DemoViewModel
import com.custom.rgs_android_dom.ui.main.MainViewModel
import com.custom.rgs_android_dom.ui.client.ClientViewModel

import com.custom.rgs_android_dom.ui.client.agent.AgentViewModel
import com.custom.rgs_android_dom.ui.client.agent.edit.EditAgentViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.edit.EditPersonalDataViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.PersonalDataViewModel
import com.custom.rgs_android_dom.ui.property.add.details.PropertyDetailsViewModel
import com.custom.rgs_android_dom.ui.property.add.select_type.SelectPropertyTypeViewModel
import com.custom.rgs_android_dom.ui.property.info.PropertyInfoViewModel
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
    viewModel { SplashViewModel(registrationInteractor = get(), translationInteractor = get()) }
    viewModel { DemoViewModel() }
    viewModel { parameters-> RegistrationAgreementViewModel(phone = parameters.get(), registrationInteractor = get()) }
    viewModel { parameters-> RegistrationFillClientViewModel(phone = parameters.get(), clientInteractor= get()) }
    viewModel { parameters-> CountriesViewModel(selectedCountryLetterCode = parameters.get(), countriesInteractor = get())}
    viewModel { ClientViewModel(clientInteractor = get(), registrationInteractor = get(), propertyInteractor = get()) }
    viewModel { MainViewModel(registrationInteractor = get()) }
    viewModel { PersonalDataViewModel(clientInteractor = get()) }
    viewModel { EditPersonalDataViewModel(clientInteractor = get()) }
    viewModel { AgentViewModel(clientInteractor = get()) }
    viewModel { EditAgentViewModel(clientInteractor = get()) }
    viewModel { AboutAppViewModel() }
    viewModel { ChatViewModel(chatInteractor = get()) }
    viewModel { parameters-> SelectPropertyTypeViewModel(propertyCount = parameters.get(), selectPropertyTypeInteractor = get()) }
    viewModel { parameters-> PropertyDetailsViewModel(propertyCount = parameters.get(), propertyType = parameters.get(), propertyInteractor = get()) }
    viewModel { parameters-> PropertyInfoViewModel(objectId = parameters.get(), propertyInteractor = get()) }

}