package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class SubtypeResponse(
    @SerializedName("archivedAt")
    val archivedAt: DateTime,

    @SerializedName("internal")
    val internal: Boolean,

    @SerializedName("logo")
    val logo: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("subtype")
    val subtype: String,

    @SerializedName("type")
    val type: String
)