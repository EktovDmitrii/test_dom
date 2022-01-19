package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class CatalogNodesResponse(
    @SerializedName("items")
    val items: List<CatalogNodeResponse>?
)