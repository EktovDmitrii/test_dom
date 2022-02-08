package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class GetContractsResponse(

    @SerializedName("contracts")
    val contracts: List<ContractResponse>? = listOf()

)
