package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.repositories.DemoRepository
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepository
import com.custom.rgs_android_dom.data.repositories.countries.MockCountriesRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.registration.MockRegistrationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import org.koin.dsl.module

val dataModule = module {

    single { DemoRepository() }
    single <CountriesRepository> { MockCountriesRepositoryImpl(api = get()) }
    single <RegistrationRepository> { MockRegistrationRepositoryImpl(api = get()) }
}