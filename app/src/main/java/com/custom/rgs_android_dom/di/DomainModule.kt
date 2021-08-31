package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.domain.DemoInteractor
import com.custom.rgs_android_dom.domain.TranslationInteractor
import com.custom.rgs_android_dom.domain.countries.CountriesInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import org.koin.dsl.module

val domainModule = module {

    factory { DemoInteractor(demoRepository = get()) }

    factory { CountriesInteractor(countriesRepository = get()) }

    factory { RegistrationInteractor(registrationRepository = get()) }

    factory { ClientInteractor(clientRepository = get(), registrationRepository = get(), countriesRepository = get()) }

    factory { TranslationInteractor(translationRepository = get()) }
}