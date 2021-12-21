package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ClientProductsResponse(

    @SerializedName("clientProducts")
    val clientProducts: List<ClientProductResponse>,

    @SerializedName("index")
    val index: Int = 0,

    @SerializedName("total")
    val total: Int = 0
)

data class ClientProductResponse(

    @SerializedName("ProductDescriptionFormat")
    val productDescriptionFormat: String,

    @SerializedName("clientId")
    val clientId: String,

    @SerializedName("contractId")
    val contractId: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("objectIds")
    val objectIds: List<String>,

    @SerializedName("productCode")
    val productCode: String,

    @SerializedName("productDescription")
    val productDescription: String,

    @SerializedName("productDescriptionRef")
    val productDescriptionRef: String,

    @SerializedName("productIcon")
    val productIcon: String,

    @SerializedName("productId")
    val productId: String,

    @SerializedName("productName")
    val productName: String,

    @SerializedName("productTitle")
    val productTitle: String,

    @SerializedName("productType")
    val productType: String,

    @SerializedName("productVersionId")
    val productVersionId: String,

    @SerializedName("status")
    val status: String = "draft",

    @SerializedName("validityFrom")
    val validityFrom: String,

    @SerializedName("validityTo")
    val validityTo: String

)
