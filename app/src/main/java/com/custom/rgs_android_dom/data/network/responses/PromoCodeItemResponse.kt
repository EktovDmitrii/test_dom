package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class PromoCodeItemResponse(

    @SerializedName("id")
    val id: String?,

    @SerializedName("promoCampaignId")
    val promoCampaignId: String?,

    @SerializedName("clientId")
    val clientId: String?,

    @SerializedName("code")
    val code: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("businessLine")
    val businessLine: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("discountInPercent")
    val discountInPercent: Int?,

    @SerializedName("discountInRubles")
    val discountInRubles: Int?,

    @SerializedName("expiredAt")
    val expiredAt: DateTime?,

    @SerializedName("products")
    val products: List<PromoCodeProductsResponse>?,

    @SerializedName("productPeriodFrom")
    val productPeriodFrom: DateTime?,

    @SerializedName("productPeriodTo")
    val productPeriodTo: DateTime?,

)

data class PromoCodeProductsResponse(

    @SerializedName("count")
    val count: Int?,

    @SerializedName("productId")
    val productId: String?,
)
