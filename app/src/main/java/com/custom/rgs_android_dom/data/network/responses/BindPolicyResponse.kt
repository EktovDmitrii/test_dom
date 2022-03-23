package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class BindPolicyResponse(

    @SerializedName("contract")
    val contract: ContractResponse?,

    @SerializedName("clientProductIds")
    val clientProductIds: List<String>?

)