package com.custom.rgs_android_dom.data.network.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

// TODO This is mocked data, just for example. We will change this when backend will be ready
class BasicAuthInterceptor : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request().newBuilder()
            .header(HEADER_AUTHORIZATION, Credentials.basic("rgsadmin", "RGSmed!!"))
            .build())


}

