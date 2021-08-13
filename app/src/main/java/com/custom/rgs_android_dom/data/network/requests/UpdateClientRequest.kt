package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateClientRequest(
    @SerializedName("agentCode")
    val agentCode: String?,

    @SerializedName("agentPhone")
    val agentPhone: String?,

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("birthdate")
    val birthdate: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("location")
    val location: UpdateLocationRequest?,

    @SerializedName("middleName")
    val middleName: String?,

    @SerializedName("opdSignedAt")
    val opdSignedAt: String?,

    @SerializedName("sex")
    val sex: String?

) {

    data class UpdateLocationRequest(

        @SerializedName("code")
        val code: String,

        @SerializedName("name")
        val name: String

    )

}