package com.custom.rgs_android_dom.data.network.responses

import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ContractResponse(

    @SerializedName("id")
    val id: String,

    @SerializedName("serial")
    val serial: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("startDate")
    val startDate: DateTime,

    @SerializedName("endDate")
    val endDate: DateTime,

    @SerializedName("clientFirstName")
    val clientFirstName: String,

    @SerializedName("clientMiddleName")
    val clientMiddleName: String,

    @SerializedName("clientLastName")
    val clientLastName: String,

    @SerializedName("clientBirthDate")
    val clientBirthDate: String

)
