package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val preferencesModule = module {

    single { AuthSharedPreferences(androidContext(), gson = get()) }

}