package com.custom.rgs_android_dom.di

import androidx.biometric.BiometricManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    factory { BiometricManager.from(androidContext()) }

}