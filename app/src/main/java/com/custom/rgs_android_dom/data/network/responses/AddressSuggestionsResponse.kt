package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class AddressSuggestionsResponse(

    @SerializedName("results")
    val results: List<AddressItemResponse>?

)