package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.DomServiceApi
import com.custom.rgs_android_dom.data.network.data_adapters.JodaDateTimeGsonTypeAdapter
import com.custom.rgs_android_dom.data.network.data_adapters.JodaLocalDateGsonTypeAdapter
import com.custom.rgs_android_dom.data.network.data_adapters.JodaLocalDateTimeGsonTypeAdapter
import com.custom.rgs_android_dom.data.network.interceptors.AuthTokenInterceptor
import com.custom.rgs_android_dom.data.network.interceptors.BasicAuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<Gson> {
        GsonBuilder()
            .setLenient()
            .registerTypeAdapter(LocalDateTime::class.java, JodaLocalDateTimeGsonTypeAdapter())
            .registerTypeAdapter(LocalDate::class.java, JodaLocalDateGsonTypeAdapter())
            .registerTypeAdapter(DateTime::class.java, JodaDateTimeGsonTypeAdapter())
            .create()
    }

    single { BasicAuthInterceptor() }

    single { AuthTokenInterceptor() }

    single {
        HttpLoggingInterceptor().apply {
            level = (HttpLoggingInterceptor.Level.BODY)
        }
    }

    single {
        OkHttpClient.Builder()
            .apply {
                followRedirects(false)
                followSslRedirects(false)

                if (BuildConfig.DEBUG) {
                    connectTimeout(60, TimeUnit.SECONDS)
                    callTimeout(60, TimeUnit.SECONDS)
                    readTimeout(60, TimeUnit.SECONDS)
                    writeTimeout(60, TimeUnit.SECONDS)
                }

                addInterceptor(get<BasicAuthInterceptor>())
                addInterceptor(get<AuthTokenInterceptor>())
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
    }

    single {
        get<Retrofit>().create(DomServiceApi::class.java)
    }

}