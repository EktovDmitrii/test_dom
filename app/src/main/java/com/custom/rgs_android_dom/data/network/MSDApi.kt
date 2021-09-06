package com.custom.rgs_android_dom.data.network

import com.custom.rgs_android_dom.data.network.data_adapters.NetworkException
import com.custom.rgs_android_dom.data.network.error.MSDNetworkError
import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.data.network.responses.AuthResponse
import com.custom.rgs_android_dom.data.network.responses.ClientResponse
import com.custom.rgs_android_dom.data.network.responses.GetCodeResponse
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
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
    fun postLogin(@Header("Authorization") token: String, @Body body: LoginRequest): Single<AuthResponse>

    @POST("auth/logout")
    @ErrorType(MSDNetworkError::class)
    fun postLogout(): Completable

    @POST("clients/{clientId}/opd/sign")
    @ErrorType(MSDNetworkError::class)
    fun postSignOpd(@Path("clientId") clientId: String): Completable

    @POST("auth/token/refresh")
    @ErrorType(MSDNetworkError::class)
    fun postRefreshToken(@Header("Authorization") refreshToken: String): Single<TokenResponse>

    @GET("clients/{clientId}")
    @ErrorType(MSDNetworkError::class)
    fun getClient(@Path("clientId") clientId: String): Single<ClientResponse>

    @PUT("clients/{clientId}")
    @ErrorType(MSDNetworkError::class)
    fun putClient(@Path("clientId") clientId: String, @Body body: UpdateClientRequest): Single<ClientResponse>

    @POST("clients/{clientId}/agents")
    @ErrorType(MSDNetworkError::class)
    fun updateAgent(@Path("clientId") clientId: String, @Body body: UpdateAgentRequest): Single<ClientResponse>

    @POST("clients/{clientId}/documents")
    @ErrorType(MSDNetworkError::class)
    fun postDocuments(@Path("clientId") clientId: String, @Body body: UpdateDocumentsRequest): Single<ClientResponse>

    @POST("clients/{clientId}/contacts")
    @ErrorType(MSDNetworkError::class)
    fun postContacts(@Path("clientId") clientId: String, @Body body: UpdateContactsRequest): Single<ClientResponse>

    @PUT("clients/{clientId}/contacts")
    @ErrorType(MSDNetworkError::class)
    fun putContacts(@Path("clientId") clientId: String, @Body body: UpdateContactsRequest): Single<ClientResponse>
}