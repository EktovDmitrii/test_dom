package com.custom.rgs_android_dom.data.network.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.requests.*
import com.custom.rgs_android_dom.data.network.responses.*
import com.custom.rgs_android_dom.domain.catalog.models.ServiceShortModel
import com.custom.rgs_android_dom.domain.client.models.*
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.policies.models.PolicyShortModel
import com.custom.rgs_android_dom.utils.formatPhoneForApi

object ClientMapper {

    const val DOCTYPE_NATIONAL_PASSPORT = "npp"
    const val CONTACT_TYPE_PHONE = "phone"
    const val CONTACT_TYPE_EMAIL = "email"
    const val CONTACT_TYPE_CHAT = "chat"

    private const val AVATAR_ENDPOINT = "${BuildConfig.BASE_URL}/api/store"

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
            agent = null,
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
            status = response.status,
            checkoutEmail = ""
        )

    }

    fun agentToRequest(code: String, phone: String, assignType: String): AssignAgentRequest {
        return AssignAgentRequest(
            agentCode = code,
            agentPhone = phone.formatPhoneForApi(),
            assignType = assignType
        )
    }

    fun passportToRequest(serial: String, number: String): PostDocumentsRequest {
        val documents = arrayListOf(
            PostDocumentRequest(serial = serial, number = number, type = DOCTYPE_NATIONAL_PASSPORT)
        )
        return PostDocumentsRequest(postDocuments = documents)
    }

    fun passportToRequest(id: String, serial: String, number: String): UpdateDocumentsRequest {
        val documents = arrayListOf(
            UpdateDocumentRequest(id = id, serial = serial, number = number, type = DOCTYPE_NATIONAL_PASSPORT)
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

    fun responseToUserDetails(response: UserResponse): UserDetailsModel{
        val avatarFileId = response.details.avatar ?: ""
        if (avatarFileId.isNotEmpty()){
            return UserDetailsModel(avatarUrl = "${AVATAR_ENDPOINT}/${avatarFileId}", avatarFileId = avatarFileId)
        }
        return UserDetailsModel(avatarUrl = "", avatarFileId = "")
    }

    fun responseToAgent(response: AgentHolderResponse?): ClientAgent? {
        return if (response != null){
            ClientAgent(
                code = response.agent.agentCode,
                phone = response.assignPhone,
                editAgentWasRequested = null
            )
        } else {
            null
        }
    }

    fun responseToPolicyShort(response: ClientProductResponse): PolicyShortModel {
        return PolicyShortModel(
            id = response.id ?: "",
            contractId = response.contractId ?: "",
            name = response.productName ?: "",
            logo = response.logoSmall,
            startsAt = response.validityFrom,
            expiresAt = response.validityTo
        )
    }

    fun responseToPolicy(clientProductResponse: ClientProductResponse?,contractResponse: ContractResponse?, propertyItemResponse: PropertyItemResponse?, productServicesResponse: ProductServicesResponse?): PolicyModel {

        return PolicyModel(
            id= clientProductResponse?.productId ?: "",
            productId = clientProductResponse?.productId ?: "",
            logo = clientProductResponse?.productIcon ?: "",
            productTitle= clientProductResponse?.productTitle ?: "",
            productDescription = clientProductResponse?.productDescription ?: "",
            address = propertyItemResponse?.address?.address ?: "",
            includedProducts = productServicesResponse?.items?.map {
                ServiceShortModel(
                    priceAmount = it.priceAmount,
                    providerId = it.providerId,
                    providerName = it.providerName,
                    quantity = it.quantity ?: 0,
                    serviceCode = it.serviceCode,
                    serviceId = it.serviceId,
                    serviceName = it.serviceName,
                    serviceDeliveryType = it.serviceDeliveryType,
                    serviceVersionId = it.serviceVersionId,
                    isPurchased = true)
            } ?: listOf(),
            policySeriesAndNumber = "${contractResponse?.serial} ${contractResponse?.number}",
            clientName = "${contractResponse?.clientLastName} ${contractResponse?.clientFirstName} ${contractResponse?.clientMiddleName}",
            startsAt = contractResponse?.startDate,
            expiresAt = contractResponse?.endDate
        )
    }

}