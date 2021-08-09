package com.custom.rgs_android_dom.data.network

import com.custom.rgs_android_dom.data.network.data_adapters.NetworkException
import com.custom.rgs_android_dom.data.network.error.MSDNetworkError
import com.custom.rgs_android_dom.data.network.requests.LoginRequest
import com.custom.rgs_android_dom.data.network.requests.GetCodeRequest
import com.custom.rgs_android_dom.data.network.responses.AuthResponse
import com.custom.rgs_android_dom.data.network.responses.GetCodeResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorType(val type: KClass<*>)

fun Throwable.toNetworkException(): MSDNetworkError?{
    return (this as? NetworkException)?.error as? MSDNetworkError
}

interface MSDApi {

    @POST("auth/clients/code")
    @ErrorType(MSDNetworkError::class)
    fun postGetCode(@Body body: GetCodeRequest): Single<GetCodeResponse>

    @POST("auth/clients/login")
    @ErrorType(MSDNetworkError::class)
    fun postLogin(@Body body: LoginRequest): Single<AuthResponse>

    @POST("auth/logount")
    @ErrorType(MSDNetworkError::class)
    fun postLogout(): Completable

}