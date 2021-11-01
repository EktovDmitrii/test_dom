package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import org.koin.dsl.module

val providersModule = module {

    single { AuthContentProviderManager(context = get()) }

}