package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {

    single { ClientSharedPreferences(context = androidContext(), gson = get()) }

}