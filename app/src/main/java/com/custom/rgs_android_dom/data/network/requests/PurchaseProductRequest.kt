package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class PurchaseProductRequest(
    @SerializedName("bindingId")
    val bindingId: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("objectId")
    val objectId: String,

    @SerializedName("saveCard")
    val saveCard: Boolean,

    @SerializedName("order")
    val order: OrderRequest?
)

data class OrderRequest(

    @SerializedName("comment")
    val comment: String? = null,

    @SerializedName("deliveryDate")
    val deliveryDate: String? = null,

    @SerializedName("deliveryTime")
    val deliveryTime: OrderTimeRequest? = null,

    @SerializedName("withOrder")
    val withOrder: Boolean = false
)

data class OrderTimeRequest(
    @SerializedName("from")
    val timeFrom: String,

    @SerializedName("to")
    val timeTo: String
)