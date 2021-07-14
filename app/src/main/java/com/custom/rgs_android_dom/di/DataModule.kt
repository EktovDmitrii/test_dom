package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.DemoRepository
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val dataModule = module {

    single { DemoRepository() }

}