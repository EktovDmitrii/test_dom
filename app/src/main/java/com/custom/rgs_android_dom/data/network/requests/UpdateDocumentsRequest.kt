package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateDocumentsRequest(
    @SerializedName("documents")
    val documents: List<DocumentRequest>
)

data class DocumentRequest(
    @SerializedName("serial")
    val serial: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("type")
    val type: String
)