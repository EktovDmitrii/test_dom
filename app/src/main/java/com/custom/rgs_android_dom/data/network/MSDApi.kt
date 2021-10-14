package com.custom.rgs_android_dom.data.network

import com.custom.rgs_android_dom.data.network.data_adapters.NetworkException
import com.custom.rgs_android_dom.data.network.error.MSDNetworkErrorResponse
import com.custom.rgs_android_dom.data.network.responses.*
import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.domain.error.model.MSDErrorModel
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorType(val type: KClass<*>)

fun Throwable.toMSDErrorModel(): MSDErrorModel?{
    return (this as? NetworkException)?.error as? MSDErrorModel
}

interface MSDApi {

    @POST("auth/clients/code")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postGetCode(@Body body: GetCodeRequest): Single<GetCodeResponse>

    @POST("auth/clients/login")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postLogin(@Header("Authorization") token: String, @Body body: LoginRequest): Single<AuthResponse>

    @POST("auth/logout")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postLogout(): Completable

    @POST("clients/{clientId}/opd/sign")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postSignOpd(@Path("clientId") clientId: String): Completable

    @POST("auth/token/refresh")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postRefreshToken(@Header("Authorization") refreshToken: String): Single<TokenResponse>

    @GET("clients/{clientId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getClient(@Path("clientId") clientId: String): Single<ClientResponse>

    @PUT("clients/{clientId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun putClient(@Path("clientId") clientId: String, @Body body: UpdateClientRequest): Single<ClientResponse>

    @GET("catalog/translations")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getTranslations(@Query("platform") platform: String,@Query("lang") lang: String, @Query("project") project: String): Single<TranslationListResponse>

    @POST("clients/{clientId}/agents")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun updateAgent(@Path("clientId") clientId: String, @Body body: UpdateAgentRequest): Single<ClientResponse>

    @POST("clients/{clientId}/documents")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postDocuments(@Path("clientId") clientId: String, @Body body: UpdateDocumentsRequest): Single<ClientResponse>

    @POST("clients/{clientId}/contacts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postContacts(@Path("clientId") clientId: String, @Body body: UpdateContactsRequest): Single<ClientResponse>

    @PUT("clients/{clientId}/contacts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun putContacts(@Path("clientId") clientId: String, @Body body: UpdateContactsRequest): Single<ClientResponse>

    @HTTP(method = "DELETE", path = "clients/{clientId}/contacts", hasBody = true)
    @ErrorType(MSDNetworkErrorResponse::class)
    fun deleteContacts(@Path("clientId") clientId: String, @Body body: DeleteContactsRequest): Single<ClientResponse>

    @POST("chat/users/{userId}/channels/{channelId}/posts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postMessage(@Path("userId") userId: String, @Path("channelId") channelId: String, @Body message: SendMessageRequest): Completable

    @GET("chat/channels/{channelId}/posts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getChatMessages(@Path("channelId") channelId: String, @Query("limit") limit: Long, @Query("offset") offset: Long): Single<List<ChatMessageResponse>>

    @POST("property/clients/{clientId}/objects")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun addProperty(@Path("clientId") clientId: String, @Body body: AddPropertyRequest): Completable

    @GET("property/clients/{clientId}/objects")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAllProperty(@Path("clientId") clientId: String): Single<AllPropertyResponse>

    @GET("property/objects/{objectId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getPropertyItem(@Path("objectId") objectId: String): Single<PropertyItemResponse>

    @GET("location/address/suggests/{address}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAddressSuggestions(@Path("address") address: String, @Query("country") country: String): Single<AddressSuggestionsResponse>

    @GET("location/address")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAddressByCoordinates(@Query("lat") lat: Double, @Query("long") long: Double): Single<AddressSuggestionsResponse>
}