package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class DemoResponse(

    @SerializedName("name")
    var name: String?,

    @SerializedName("count")
    var count: Long?

) : Serializable
