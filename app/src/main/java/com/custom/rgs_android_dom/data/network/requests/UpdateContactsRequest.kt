package com.custom.rgs_android_dom.data.network.requests

import com.google.gson.annotations.SerializedName

data class UpdateContactsRequest(

    @SerializedName("contacts")
    val contacts: List<ContactRequest>
)


data class ContactRequest(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("contact")
    val contact: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("preferred")
    val preferred: Boolean? = null,

    @SerializedName("verified")
    val verified: Boolean? = null

)
