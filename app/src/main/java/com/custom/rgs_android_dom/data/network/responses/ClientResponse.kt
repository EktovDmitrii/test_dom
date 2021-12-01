package com.custom.rgs_android_dom.data.network.responses

import com.custom.rgs_android_dom.domain.client.models.Gender
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class ClientResponse (
	@SerializedName("userId")
	val userId: String,

	@SerializedName("addresses")
	val addresses: List<AddressResponse>?,

	@SerializedName("birthDate")
	val birthDate: DateTime?,

	@SerializedName("contacts")
	val contacts: List<ContactResponse>?,

	@SerializedName("documents")
	val documents: List<DocumentResponse>?,

	@SerializedName("firstName")
	val firstName: String?,

	@SerializedName("id") val id: String?,

	@SerializedName("lastName")
	val lastName: String?,

	@SerializedName("location")
	val location: LocationResponse?,

	@SerializedName("middleName")
	val middleName: String?,

	@SerializedName("opdAgreement")
	val opdAgreement: OpdAgreementResponse?,

	@SerializedName("phone")
	val phone: String,

	@SerializedName("sex")
	val gender: Gender?,

	@SerializedName("status")
	val status: String?,

	@SerializedName("email")
	val email: String,

	@SerializedName("secondPhone")
	val secondPhone: String,

	@SerializedName("docSerial")
	val  docSerial: String,

	@SerializedName("docNumber")
	val docNumber: String

	)

data class AddressResponse (
	@SerializedName("id")
	val id: String,

	@SerializedName("address")
	val address: String?,

	@SerializedName("lat")
	val lat: Int?,

	@SerializedName("long")
	val long: Int?,

	@SerializedName("preferred")
	val preferred: Boolean?,

	@SerializedName("type")
	val type: String?
)

data class ContactResponse (
	@SerializedName("id")
	val id: String,

	@SerializedName("contact") 
	val contact: String?,
	
	@SerializedName("preferred")
	val preferred: Boolean?,
	
	@SerializedName("type") 
	val type: String?,
	
	@SerializedName("verified") 
	val verified: Boolean?
)

data class DocumentResponse (
	@SerializedName("id")
	val id: String,

	@SerializedName("expireAt") 
	val expireAt: String?,
	
	@SerializedName("fileIds") 
	val fileIds: List<String>?,
	
	@SerializedName("issuedAt")
	val issuedAt: String?,
	
	@SerializedName("issuedBy") 
	val issuedBy: String?,
	
	@SerializedName("number") 
	val number: String?,
	
	@SerializedName("preferred") 
	val preferred: Boolean?,
	
	@SerializedName("serial") 
	val serial: String?,
	
	@SerializedName("type") 
	val type: String?
)

data class LocationResponse (
	@SerializedName("code")
	val code: String?,
	
	@SerializedName("name") 
	val name: String?
)

data class OpdAgreementResponse (
	@SerializedName("revokedAt") 
	val revokedAt: String?,
	
	@SerializedName("signedAt") 
	val signedAt: String?
)