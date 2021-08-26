package com.custom.rgs_android_dom.profile

import com.custom.rgs_android_dom.data.repositories.client.ClientRepository
import com.custom.rgs_android_dom.domain.client.models.ClientAgent
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.ClientOpdAgreement
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.util.*

class MockClientRepository: ClientRepository {

    private var clientModel: ClientModel? = null
    private val clientUpdatedSubject: BehaviorRelay<ClientModel> = BehaviorRelay.create()

    companion object {
        val PHONE = "78888888888"
        val PHONEMASKED = "+7 888 888-88-88"
        val NAME = "ИВАН"
        val SURNAME = "ИВАНОВ"
        val BIRTHDATE = LocalDateTime(2002, 10, 3,0,0)
        val BIRTHDATESTR = "03.10.2002"
        val GENDER = Gender.FEMALE
        val AGENTCODE = "777"
        val PHONEAGENT = "79044961128"
    }

    private val mockClient = ClientModel(
        userId = "1",
        id = "1",
        addresses = null,
        agent = ClientAgent(AGENTCODE, PHONEAGENT),
        birthDate = BIRTHDATE.toDateTime(),
        contacts = null,
        documents = null,
        firstName = NAME,
        lastName = SURNAME,
        location = null,
        middleName = null,
        opdAgreement = ClientOpdAgreement(null, null),
        phone = PHONE,
        gender = GENDER,
        status = null
    )



    override fun updateClient(
        firstName: String?,
        lastName: String?,
        birthday: LocalDateTime?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Completable {
        return Completable.fromCallable {
            Thread.sleep(3000)
            clientModel = ClientModel(
            userId = "1",
                id = "1",
                addresses = null,
               agent = ClientAgent(agentCode, agentPhone),
            birthDate = birthday?.toDateTime(),
            contacts = null,
            documents = null,
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            location = null,
            middleName = null,
            opdAgreement = ClientOpdAgreement(null, null),
                phone = PHONE,
                gender = gender,
                status = null
            )
            clientModel
        }.doOnComplete {
            clientUpdatedSubject.accept(clientModel!!)
        }
    }

    override fun getClient(): Single<ClientModel> {
        return Single.just(clientModel)
    }

    override fun loadAndSaveClient(): Completable {
        clientModel = mockClient
        clientUpdatedSubject.accept(clientModel!!)
        return Completable.complete()
    }

    override fun getClientUpdatedSubject(): Observable<ClientModel> {
        return clientUpdatedSubject.hide()
    }
}