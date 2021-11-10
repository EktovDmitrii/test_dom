package com.custom.rgs_android_dom.data.repositories.client

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.data.network.requests.DeleteContactsRequest
import com.custom.rgs_android_dom.data.network.requests.UpdateClientRequest
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.repositories.ClientRepository
import com.custom.rgs_android_dom.utils.PATTERN_DATE_TIME_MILLIS
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import com.custom.rgs_android_dom.utils.formatTo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.LocalDateTime

class ClientRepositoryImpl(
    private val api: MSDApi,
    private val clientSharedPreferences: ClientSharedPreferences
) : ClientRepository {

    private val clientUpdatedSubject: PublishSubject<ClientModel> = PublishSubject.create()
    private val editAgentRequestedSubject: PublishSubject<Boolean> = PublishSubject.create()

    override fun updateClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        birthday: LocalDateTime?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?,
        phone: String?,
        email: String?,
        avatar: String?
    ): Completable {
        val request = UpdateClientRequest(
            agentCode = agentCode,
            agentPhone = agentPhone?.formatPhoneForApi(),
            avatar = avatar,
            birthdate = birthday?.formatTo(PATTERN_DATE_TIME_MILLIS),
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            sex = gender?.shortcode,
            phone = phone,
            email = email
        )
        return api.putMyClient(request)
            .flatMapCompletable { response ->
                val client = ClientMapper.responseToClient(response)
                clientSharedPreferences.saveClient(client)
                clientUpdatedSubject.onNext(client)
                Completable.complete()
            }
    }

    override fun getClient(): Single<ClientModel> {
        if (clientSharedPreferences.getClient() != null) {
            return Single.fromCallable {
                clientSharedPreferences.getClient()
            }
        } else {
            return api.getMyClient().map { response ->
                val client = ClientMapper.responseToClient(response)
                clientSharedPreferences.saveClient(client)
                return@map client
            }
        }
    }


    override fun loadAndSaveClient(): Completable {
        return api.getMyClient().flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)

            val cachedClient = clientSharedPreferences.getClient()
            if (cachedClient != null && cachedClient != client || cachedClient == null){
                clientSharedPreferences.saveClient(client)
                clientUpdatedSubject.onNext(client)
            }

            Completable.complete()
        }
    }

    override fun getClientUpdatedSubject(): Observable<ClientModel> {
        return clientUpdatedSubject.hide()
    }

    override fun updateAgent(code: String, phone: String): Completable {
        val agentRequest = ClientMapper.agentToRequest(code, phone)
        return api.updateAgent(agentRequest)
            .flatMapCompletable {response->
                val client = ClientMapper.responseToClient(response)
                clientSharedPreferences.saveClient(client)
                clientUpdatedSubject.onNext(client)
                Completable.complete()
            }
    }

    override fun updatePassport(serial: String, number: String): Completable {
        val updateDocumentsRequest = ClientMapper.passportToRequest(serial, number)
        return api.postDocuments(updateDocumentsRequest).flatMapCompletable {response->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }

    override fun saveSecondPhone(phone: String): Completable {
        val updateContactsRequest = ClientMapper.phoneToRequest(phone.formatPhoneForApi())
        return api.postContacts(updateContactsRequest).flatMapCompletable { response->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }

    override fun updateSecondPhone(phone: String, id: String): Completable {
        val updateContactsRequest = ClientMapper.phoneToRequest(phone.formatPhoneForApi(), id)
        return api.putContacts(updateContactsRequest).flatMapCompletable { response->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }

    override fun deleteContacts(ids: ArrayList<String>): Completable {
        val deleteContactsRequest = DeleteContactsRequest(ids = ids)
        return api.deleteContacts(deleteContactsRequest).flatMapCompletable { response->
            val client = ClientMapper.responseToClient(response)
            clientSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }

    override fun requestEditAgent(): Completable {
        // Todo Replace with real request later
        return Completable.fromCallable{
            Thread.sleep(2000)
            editAgentRequestedSubject.onNext(true)
        }
    }

    override fun getEditAgentRequestedSubject(): PublishSubject<Boolean> {
        return editAgentRequestedSubject
    }
}