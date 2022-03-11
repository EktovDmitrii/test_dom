package com.custom.rgs_android_dom.ui

import android.app.Application
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.di.*
import com.yandex.mapkit.MapKitFactory
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        activateAppMetrica()
        initKoin()
        JodaTimeAndroid.init(this)
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_KEY)
    }

    private fun initKoin() {
        startKoin {

            androidContext(this@App)

            modules(
                appModule,
                dataModule,
                domainModule,
                networkModule,
                viewModelModule,
                preferencesModule,
                providersModule,
                otherModule
            )
        }
    }

    private fun activateAppMetrica() {
        val appMetricaConfig: YandexMetricaConfig =
            YandexMetricaConfig.newConfigBuilder(BuildConfig.YANDEX_APPMETRICA_KEY)
                .withLocationTracking(true)
                .withStatisticsSending(true)
                .build()
        YandexMetrica.activate(applicationContext, appMetricaConfig)
        YandexMetrica.enableActivityAutoTracking(this)
    }
}