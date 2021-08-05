package com.custom.rgs_android_dom.data.network.provider

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.MyServiceDomApi
import com.custom.rgs_android_dom.data.network.data_adapters.RxCallAdapterWrapperFactory
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiProvider(
    private val interceptors: List<Interceptor>,
    private val gson: Gson
) {

    private lateinit var domApi: MyServiceDomApi

    init {
        initMyServiceApi()
    }

    fun getApi(): MyServiceDomApi {
        return domApi
    }

    private fun initMyServiceApi() {
        domApi = Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory( RxCallAdapterWrapperFactory.createAsync())
            .client(
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

                        interceptors.forEach { addInterceptor(it) }
                    }
                    .build()
            )
            .build()
            .create(MyServiceDomApi::class.java)
    }
}