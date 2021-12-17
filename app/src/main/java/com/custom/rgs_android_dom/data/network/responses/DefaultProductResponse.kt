package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class DefaultProductResponse(

    @SerializedName("enabled")
    val enabled: Boolean?,

    @SerializedName("productId")
    val productId: String?,

    @SerializedName("productName")
    val productName: String?,

    @SerializedName("serviceConsultingId")
    val serviceConsultingId: String?,

    @SerializedName("serviceConsultingName")
    val serviceConsultingName: String?,

    @SerializedName("tags")
    val tags: List<String>?
)