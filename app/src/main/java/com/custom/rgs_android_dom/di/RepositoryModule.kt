package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.repositories.CountriesRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { CountriesRepository(api = get()) }

}