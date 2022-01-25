package com.custom.rgs_android_dom.data.network

import com.custom.rgs_android_dom.data.network.data_adapters.NetworkException
import com.custom.rgs_android_dom.data.network.error.MSDNetworkErrorResponse
import com.custom.rgs_android_dom.data.network.responses.*
import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.domain.error.model.MSDErrorModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MultipartBody
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

    @POST("clients/me/opd/sign")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postSignOpd(): Completable

    @POST("auth/token/refresh")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postRefreshToken(@Header("Authorization") refreshToken: String): Single<TokenResponse>

    @GET("clients/me")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getMyClient(): Single<ClientResponse>

    @PUT("clients/me")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun putMyClient(@Body body: UpdateClientRequest): Single<ClientResponse>

    @GET("catalog/translations")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getTranslations(@Query("platform") platform: String,@Query("lang") lang: String, @Query("project") project: String): Single<TranslationListResponse>

    @POST("clients/me/documents")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postDocuments(@Body body: PostDocumentsRequest): Single<ClientResponse>

    @PUT("clients/me/documents")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun updateDocuments(@Body body: UpdateDocumentsRequest): Single<ClientResponse>

    @POST("clients/me/contacts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postContacts(@Body body: UpdateContactsRequest): Single<ClientResponse>

    @PUT("clients/me/contacts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun putContacts(@Body body: UpdateContactsRequest): Single<ClientResponse>

    @HTTP(method = "DELETE", path = "clients/me/contacts", hasBody = true)
    @ErrorType(MSDNetworkErrorResponse::class)
    fun deleteContacts(@Body body: DeleteContactsRequest): Single<ClientResponse>

    @POST("chat/users/me/channels/{channelId}/posts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postMessage(@Path("channelId") channelId: String, @Body message: SendMessageRequest): Completable

    @GET("chat/channels/{channelId}/posts")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getChatMessages(@Path("channelId") channelId: String, @Query("size") size: Long, @Query("index") index: Long): Single<List<ChatMessageResponse>>

    @POST("property/clients/me/objects")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun addProperty(@Body body: AddPropertyRequest): Completable

    @PUT("property/clients/me/objects/{objectId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun updatePropertyItem(@Path("objectId") objectId: String ,@Body body: UpdatePropertyRequest): Single<PropertyItemResponse>

    @GET("property/clients/me/objects")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAllProperty(): Single<AllPropertyResponse>

    @GET("property/objects/{objectId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getPropertyItem(@Path("objectId") objectId: String): Single<PropertyItemResponse>

    @GET("location/address/suggests/{address}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAddressSuggestions(@Path("address") address: String, @Query("country") country: String): Single<AddressSuggestionsResponse>

    @GET("location/address")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAddressByCoordinates(@Query("lat") lat: Double, @Query("long") long: Double): Single<AddressSuggestionsResponse>

    @Multipart
    @POST("store")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun putFileToTheStore(@Part file: MultipartBody.Part, @Query("bucketName") bucketName: String): Single<PutFileResponse>

    @DELETE("store/{fileID}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun deleteFileFromStore(@Path("fileID") fileId: String): Completable

    @GET("auth/users/me")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getUser(): Single<UserResponse>

    @GET("chat/channels/{channelId}/members")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getChannelMembers(@Path("channelId") channelId: String): Maybe<List<ChannelMemberResponse>>

    @Multipart
    @POST("chat/users/me/channels/{channelId}/files")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postFileInChat(@Part file: MultipartBody.Part, @Path("channelId") channel: String): Single<ChatFileResponse>

    @POST("chat/users/me/channels/{channelId}/webrtc/calls")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun startCall(@Path("channelId") channelId: String): Single<CallInfoResponse>

    @POST("insurance/clients/me/agents")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun assignAgent(@Body request: AssignAgentRequest): Single<AgentHolderResponse>

    @GET("insurance/clients/me/agents")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getAgent(): Maybe<AgentHolderResponse?>

    @Multipart
    @POST("store")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun postPropertyDocument(@Part file: MultipartBody.Part,
                             @Query("bucketName") bucketName: String,
                             @Query("extension") extension: String,
                             @Query("metadata") metadata: String)
    : Single<PostPropertyDocumentResponse>

    @GET("services/catalog/search/query")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getCatalogNodes(@Query("rootNodeId") rootNodeId: String?, @Query("rootNodeCode") rootNodeCode: String?): Single<CatalogNodesResponse>

    @GET("guests/purchase/catalog")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getGuestCatalogNodes(@Query("rootNodeId") rootNodeId: String?, @Query("rootNodeCode") rootNodeCode: String?): Single<CatalogNodesResponse>

    @GET("services/search/query")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getServices(@Query("size") size: Int, @Query("index") index: Int): Single<ServiceItemsResponse>

    @GET("products/search/query")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getProducts(@Query("size") size: Int, @Query("index") index: Int): Single<ProductItemsResponse>

    @GET("clients/me/purchase/products/showcase")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getShowcase(@Query("tags", encoded = true) tags: String?, @Query("index") index: Int, @Query("size") size: Long): Single<ProductsForPurchaseResponse>

    @GET("guests/purchase/products/showcase")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getGuestShowcase(@Query("tags", encoded = true) tags: String?, @Query("index") index: Int, @Query("size") size: Long): Single<ProductsForPurchaseResponse>

    @GET("products/{productId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getProduct(@Path("productId") productId: String): Single<ProductResponse>

    @GET("clients/me/balance/products")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getClientProducts(): Single<ClientProductsResponse>

    @GET("guests/purchase/products/{productId}/services/{serviceId}/details")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getGuestServiceDetails(@Path("productId") productId: String, @Path("serviceId") serviceId: String): Single<ServiceResponse>

    @GET("guests/purchase/products/{productId}/services/{serviceId}/details")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getGuestProductServicesResponse(@Path("productId") productId: String, @Query("size") size: Int, @Query("index") index: Int): Single<ProductServicesResponse>

    @GET("clients/me/purchase/products/{productId}/services")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getProductServicesResponse(@Path("productId") productId: String, @Query("size") size: Int, @Query("index") index: Int): Single<ProductServicesResponse>

    @GET("chat/users/{userId}/files/{fileId}/preview")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getChatFilePreview(@Path ("userId") userId: String, @Path("fileId") fileId: String): Single<ChatFilePreviewResponse>

    @GET("billing/clients/me/cards")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun getSavedCards(): Single<List<SavedCardResponse>>

    @POST("clients/me/purchase/products/{productId}")
    @ErrorType(MSDNetworkErrorResponse::class)
    fun makeProductPurchase(@Path("productId") productId: String, @Body order: PurchaseProductRequest) : Single<PurchaseResponse>
}
