package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName

data class AllPropertyResponse(

    @SerializedName("objects")
    val objects: List<PropertyItemResponse>?

)