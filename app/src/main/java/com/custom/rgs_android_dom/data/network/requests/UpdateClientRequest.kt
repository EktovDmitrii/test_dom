package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateClientRequest(
    @SerializedName("agentCode")
    val agentCode: String? = null,

    @SerializedName("agentPhone")
    val agentPhone: String? = null,

    @SerializedName("avatar")
    val avatar: String? = null,

    @SerializedName("birthdate")
    val birthdate: String? = null,

    @SerializedName("docNumber")
    val docNumber: String? = null,

    @SerializedName("docSerial")
    val docSerial: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("location")
    val location: UpdateLocationRequest? = null,

    @SerializedName("middleName")
    val middleName: String? = null,

    @SerializedName("opdSignedAt")
    val opdSignedAt: String? = null,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("secondPhone")
    val secondPhone: String? = null,

    @SerializedName("sex")
    val sex: String? = null

) {

    data class UpdateLocationRequest(

        @SerializedName("code")
        val code: String,

        @SerializedName("name")
        val name: String

    )

}