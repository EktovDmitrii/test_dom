package com.custom.rgs_android_dom.domain.client.models

import org.joda.time.DateTime

data class ClientModel (
	val userId: String,
	val id: String?,
	val addresses: List<ClientAddress>?,
	val agent: ClientAgent?,
	val birthDate: DateTime?,
	val contacts: List<ClientContact>?,
	val documents: List<ClientDocument>?,
	val firstName: String,
	val lastName: String,
	val location: ClientLocation?,
	val middleName: String?,
	val opdAgreement: ClientOpdAgreement?,
	val phone: String,
	val gender: Gender?,
	val status: String?
)




