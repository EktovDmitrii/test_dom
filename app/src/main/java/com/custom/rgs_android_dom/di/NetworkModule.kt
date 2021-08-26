package com.custom.rgs_android_dom.di

import com.custom.rgs_android_dom.data.network.data_adapters.GenderGsonAdapter
import com.custom.rgs_android_dom.data.network.data_adapters.JodaDateTimeGsonTypeAdapter
import com.custom.rgs_android_dom.data.network.data_adapters.JodaLocalDateGsonTypeAdapter
import com.custom.rgs_android_dom.data.network.data_adapters.JodaLocalDateTimeGsonTypeAdapter
import com.custom.rgs_android_dom.data.network.interceptors.AuthTokenInterceptor
import com.custom.rgs_android_dom.data.network.interceptors.BasicAuthInterceptor
import com.custom.rgs_android_dom.data.network.provider.ApiProvider
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.koin.dsl.module

val networkModule = module {

    single<Gson> {
        GsonBuilder()
            .setLenient()
            .registerTypeAdapter(LocalDateTime::class.java, JodaLocalDateTimeGsonTypeAdapter())
            .registerTypeAdapter(LocalDate::class.java, JodaLocalDateGsonTypeAdapter())
            .registerTypeAdapter(DateTime::class.java, JodaDateTimeGsonTypeAdapter())
            .registerTypeAdapter(Gender::class.java, GenderGsonAdapter())
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
        ApiProvider(
            interceptors = listOf(
                get<AuthTokenInterceptor>(),
                /*get<BasicAuthInterceptor>(),*/
                get<HttpLoggingInterceptor>()
            ),
            gson = get()
        )
    }

    single { get<ApiProvider>().getApi() }

}