package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.data.network.responses.ClientResponse
import com.custom.rgs_android_dom.domain.client.models.*
import com.custom.rgs_android_dom.utils.formatPhoneForApi

object ClientMapper {

    const val DOCTYPE_NATIONAL_PASSPORT = "npp"
    const val CONTACT_TYPE_PHONE = "phone"
    const val CONTACT_TYPE_EMAIL = "email"

    fun responseToClient(response: ClientResponse): ClientModel {

        return ClientModel(
            userId = response.userId,
            id = response.id,
            addresses = response.addresses?.map {
                ClientAddress(
                    id = it.id,
                    address = it.address,
                    lat = it.lat,
                    long = it.long,
                    preferred = it.preferred,
                    type = it.type
                )
            },
            agent = ClientAgent(
                code = response.agent?.code,
                phone = response.agent?.phone
            ),
            birthDate = response.birthDate,
            contacts = response.contacts?.map {
                ClientContact(
                    id = it.id,
                    contact = it.contact,
                    preferred = it.preferred,
                    type = it.type,
                    verified = it.verified
                )
            },
            documents = response.documents?.map {
                ClientDocument(
                    id = it.id,
                    expireAt = it.expireAt,
                    fileIds = it.fileIds,
                    issuedAt = it.issuedAt,
                    issuedBy = it.issuedBy,
                    number = it.number,
                    preferred = it.preferred,
                    serial = it.serial,
                    type = it.type
                )
            },
            firstName = response.firstName ?: "",
            lastName = response.lastName ?: "",
            location = ClientLocation(
                code = response.location?.code,
                name = response.location?.name
            ),
            middleName = response.middleName,
            opdAgreement = ClientOpdAgreement(
                revokedAt = response.opdAgreement?.revokedAt,
                signedAt = response.opdAgreement?.signedAt
            ),
            phone = response.phone,
            gender = response.gender,
            status = response.status
        )

    }

    fun agentToRequest(code: String, phone: String): UpdateAgentRequest {
        return UpdateAgentRequest(
            code = code,
            phone = phone.formatPhoneForApi()
        )
    }

    fun passportToRequest(serial: String, number: String): UpdateDocumentsRequest {
        val documents = arrayListOf(
            DocumentRequest(serial = serial, number = number, type = DOCTYPE_NATIONAL_PASSPORT)
        )
        return UpdateDocumentsRequest(documents = documents)
    }

    fun phoneToRequest(phone: String): UpdateContactsRequest {
        val contacts = arrayListOf(
            ContactRequest(
                contact = phone,
                type = CONTACT_TYPE_PHONE
            )
        )
        return UpdateContactsRequest(contacts = contacts)
    }

    fun phoneToRequest(phone: String, id: String): UpdateContactsRequest {
        val contacts = arrayListOf(
            ContactRequest(
                id = id,
                contact = phone,
                type = CONTACT_TYPE_PHONE
            )
        )
        return UpdateContactsRequest(contacts = contacts)
    }

}