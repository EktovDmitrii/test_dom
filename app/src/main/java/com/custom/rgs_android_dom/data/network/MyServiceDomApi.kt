package com.custom.rgs_android_dom.data.network

import com.custom.rgs_android_dom.data.network.data_adapters.NetworkException
import com.custom.rgs_android_dom.data.network.error.MSDNetworkError
import com.custom.rgs_android_dom.data.network.requests.AuthRequest
import com.custom.rgs_android_dom.data.network.responses.AuthResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorType(val type: KClass<*>)

fun Throwable.toNetworkException(): MSDNetworkError?{
    return (this as? NetworkException)?.error as? MSDNetworkError
}

interface MyServiceDomApi {

    @POST("auth/login")
    @ErrorType(MSDNetworkError::class)
    fun postLogin(@Body body: AuthRequest): Single<AuthResponse>

    @POST("auth/users/{un}/code")
    @ErrorType(MSDNetworkError::class)
    fun postRequestCode(@Path("un") username: String): Completable
}