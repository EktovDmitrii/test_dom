package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class ClientCasesResponse(
    @SerializedName("active")
    val activeCases: List<CaseResponse>?,

    @SerializedName("archived")
    val archivedCases: List<CaseResponse>?
)