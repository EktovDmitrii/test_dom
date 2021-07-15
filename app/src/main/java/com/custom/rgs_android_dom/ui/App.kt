package com.custom.rgs_android_dom.ui

import android.app.Application
import com.custom.rgs_android_dom.di.appModule
import com.custom.rgs_android_dom.di.dataModule
import com.custom.rgs_android_dom.di.domainModule
import com.custom.rgs_android_dom.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()

    }

    private fun initKoin() {
        startKoin {

            androidContext(this@App)

            modules(
                appModule,
                dataModule,
                domainModule,
                networkModule
            )
        }
    }
}