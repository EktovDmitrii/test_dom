package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class BindPolicyRequest(

    @SerializedName("contractClientBirthDate")
    val contractClientBirthDate: String = "",

    @SerializedName("contractClientFirstName")
    val contractClientFirstName: String = "",

    @SerializedName("contractClientLastName")
    val contractClientLastName: String = "",

    @SerializedName("contractClientMiddleName")
    val contractClientMiddleName: String = "",

    @SerializedName("contractNumber")
    val contractNumber: String = "",

    @SerializedName("contractSerial")
    val contractSerial: String = ""

)
