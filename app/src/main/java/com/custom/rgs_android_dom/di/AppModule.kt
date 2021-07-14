package com.custom.rgs_android_dom.di

import androidx.biometric.BiometricManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    factory { BiometricManager.from(androidContext()) }

}