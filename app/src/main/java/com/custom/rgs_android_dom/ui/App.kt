package com.custom.rgs_android_dom.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.di.*
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.utils.logException
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.mapkit.MapKitFactory
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class App: Application() {

    private val clientInteractor: ClientInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        FirebaseApp.initializeApp(this)
        activateAppMetrica()
        initKoin()
        JodaTimeAndroid.init(this)
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_KEY)
        obtainAndSaveFCMToken()
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

    private fun obtainAndSaveFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {task->
            if (task.isSuccessful){
                FirebaseInstallations.getInstance().id.addOnSuccessListener {deviceId->
                    clientInteractor.saveFCMToken(task.result, deviceId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(
                            onError = {
                                logException(this, it)
                            }
                        )
                }
            }
        }
    }
}