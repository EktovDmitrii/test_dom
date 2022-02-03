package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ClientProductResponse(

    @SerializedName("ProductDescriptionFormat")
    val productDescriptionFormat: String?,

    @SerializedName("clientId")
    val clientId: String?,

    @SerializedName("contractId")
    val contractId: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("objectId")
    val objectId: String?,

    @SerializedName("productCode")
    val productCode: String?,

    @SerializedName("productDescription")
    val productDescription: String?,

    @SerializedName("productDescriptionRef")
    val productDescriptionRef: String?,

    @SerializedName("productIcon")
    val productIcon: String?,

    @SerializedName("logoSmall")
    val logoSmall: String?,

    @SerializedName("logoMiddle")
    val logoMiddle: String?,

    @SerializedName("logoLarge")
    val logoLarge: String?,

    @SerializedName("productId")
    val productId: String?,

    @SerializedName("productName")
    val productName: String?,

    @SerializedName("productTitle")
    val productTitle: String?,

    @SerializedName("productType")
    val productType: String?,

    @SerializedName("productVersionId")
    val productVersionId: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("validityFrom")
    val validityFrom: DateTime?,

    @SerializedName("validityTo")
    val validityTo: DateTime?
)