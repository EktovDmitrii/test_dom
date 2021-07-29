package com.custom.rgs_android_dom.ui

import android.app.Application
import com.custom.rgs_android_dom.di.*
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        JodaTimeAndroid.init(this)
    }

    private fun initKoin() {
        startKoin {

            androidContext(this@App)

            modules(
                appModule,
                dataModule,
                domainModule,
                networkModule,
                viewModelModule
            )
        }
    }
}