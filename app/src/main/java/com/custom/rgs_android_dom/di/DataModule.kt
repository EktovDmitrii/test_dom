package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.repositories.CountriesRepository
import com.custom.rgs_android_dom.data.repositories.DemoRepository
import com.custom.rgs_android_dom.data.repositories.RegistrationRepository
import org.koin.dsl.module

val dataModule = module {

    single { DemoRepository() }
    single { CountriesRepository(api = get()) }
    single { RegistrationRepository(api = get()) }
}