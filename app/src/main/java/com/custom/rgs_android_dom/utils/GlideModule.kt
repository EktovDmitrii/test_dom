package com.custom.rgs_android_dom.utils

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.interceptors.AuthTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.InputStream

@GlideModule
open class GlideModule : AppGlideModule() {

    //private val basicAuthInterceptor: BasicAuthInterceptor = BasicAuthInterceptor()
    private val authTokenInterceptor: AuthTokenInterceptor = AuthTokenInterceptor()

    override fun applyOptions(context: Context, glideBuilder: GlideBuilder) {
        glideBuilder
            .setDefaultRequestOptions(
                RequestOptions()
                    .format(DecodeFormat.PREFER_ARGB_8888)
            )

        if (BuildConfig.DEBUG) {
            glideBuilder.setLogLevel(Log.VERBOSE)
        }
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClientBuilder = OkHttpClient.Builder()
            //.addInterceptor(basicAuthInterceptor)
            .addInterceptor(authTokenInterceptor)

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                }
            )
        }

        registry
            .append(
                GlideUrl::class.java,
                InputStream::class.java,
                OkHttpUrlLoader.Factory(okHttpClientBuilder.build())
            )
    }

}