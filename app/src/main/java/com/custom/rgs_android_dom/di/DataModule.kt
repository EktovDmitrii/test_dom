package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.repositories.DemoRepository
import com.custom.rgs_android_dom.domain.repositories.ClientRepository
import com.custom.rgs_android_dom.data.repositories.client.ClientRepositoryImpl
import com.custom.rgs_android_dom.domain.repositories.CountriesRepository
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepositoryMock
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepositoryImpl
import com.custom.rgs_android_dom.data.repositories.translation.TranslationRepositoryImpl
import com.custom.rgs_android_dom.domain.repositories.TranslationRepository
import org.koin.dsl.module

val dataModule = module {
    single { DemoRepository() }
    single <RegistrationRepository> { RegistrationRepositoryImpl(api = get(), authSharedPreferences = get()) }
    single <ClientRepository> {ClientRepositoryImpl(api = get(), authSharedPreferences = get())}
    single <CountriesRepository> { CountriesRepositoryMock() }
    single <TranslationRepository> { TranslationRepositoryImpl(database = get(), api = get()) }
}