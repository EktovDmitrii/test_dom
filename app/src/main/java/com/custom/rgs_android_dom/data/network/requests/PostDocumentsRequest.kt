package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class PostDocumentsRequest(
    @SerializedName("documents")
    val postDocuments: List<PostDocumentRequest>
)

data class PostDocumentRequest(
    @SerializedName("serial")
    val serial: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("type")
    val type: String
)