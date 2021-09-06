package com.custom.rgs_android_dom.data.repositories.client

import android.util.Log
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ClientMapper
import com.custom.rgs_android_dom.data.network.requests.UpdateClientRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.utils.PATTERN_DATE_TIME_MILLIS
import com.custom.rgs_android_dom.utils.formatPhoneForApi
import com.custom.rgs_android_dom.utils.formatTo
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.LocalDateTime

class ClientRepositoryImpl(
    private val api: MSDApi,
    private val authSharedPreferences: AuthSharedPreferences
) : ClientRepository {

    private val clientUpdatedSubject: PublishSubject<ClientModel> = PublishSubject.create()

    override fun updateClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        birthday: LocalDateTime?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?,
        phone: String?,
        email: String?
    ): Completable {
        val request = UpdateClientRequest(
            agentCode = agentCode,
            agentPhone = agentPhone?.formatPhoneForApi(),
            birthdate = birthday?.formatTo(PATTERN_DATE_TIME_MILLIS),
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            sex = gender?.shortcode,
            phone = phone,
            email = email
        )
        return api.putClient(authSharedPreferences.getClientId(), request)
            .flatMapCompletable { response ->
                val client = ClientMapper.responseToClient(response)
                authSharedPreferences.saveClient(client)
                clientUpdatedSubject.onNext(client)
                Completable.complete()
            }
    }

    override fun getClient(): Single<ClientModel> {
        if (authSharedPreferences.getClient() != null) {
            return Single.fromCallable {
                authSharedPreferences.getClient()
            }
        } else {
            return api.getClient(authSharedPreferences.getClientId()).map { response ->
                val client = ClientMapper.responseToClient(response)
                authSharedPreferences.saveClient(client)
                return@map client
            }
        }
    }


    override fun loadAndSaveClient(): Completable {
        return api.getClient(authSharedPreferences.getClientId()).flatMapCompletable { response ->
            val client = ClientMapper.responseToClient(response)
            Log.d("MyLog", "LOAD AND SAVE CLIENT " + client.phone + " " + client.agent?.code + " " + client.agent?.phone)
            authSharedPreferences.getClient()?.let { clientCached ->
                if (clientCached != client) {
                    authSharedPreferences.saveClient(client)
                    clientUpdatedSubject.onNext(client)
                }
            }

            Completable.complete()
        }
    }

    override fun getClientUpdatedSubject(): Observable<ClientModel> {
        return clientUpdatedSubject.hide()
    }

    override fun updateAgent(code: String, phone: String): Completable {
        val agentRequest = ClientMapper.agentToRequest(code, phone)
        return api.updateAgent(authSharedPreferences.getClientId(), agentRequest)
            .flatMapCompletable {response->
                val client = ClientMapper.responseToClient(response)
                authSharedPreferences.saveClient(client)
                clientUpdatedSubject.onNext(client)
                Completable.complete()
            }
    }

    override fun updatePassport(serial: String, number: String): Completable {
        val updateDocumentsRequest = ClientMapper.passportToRequest(serial, number)
        val clientId = authSharedPreferences.getClientId()
        return api.postDocuments(clientId, updateDocumentsRequest).flatMapCompletable {response->
            val client = ClientMapper.responseToClient(response)
            authSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }

    override fun saveSecondPhone(phone: String): Completable {
        val updateContactsRequest = ClientMapper.phoneToRequest(phone.formatPhoneForApi())
        val clientId = authSharedPreferences.getClientId()
        return api.postContacts(clientId, updateContactsRequest).flatMapCompletable { response->
            val client = ClientMapper.responseToClient(response)
            authSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }

    override fun updateSecondPhone(phone: String, id: String): Completable {
        val updateContactsRequest = ClientMapper.phoneToRequest(phone.formatPhoneForApi(), id)
        val clientId = authSharedPreferences.getClientId()
        return api.putContacts(clientId, updateContactsRequest).flatMapCompletable { response->
            val client = ClientMapper.responseToClient(response)
            authSharedPreferences.saveClient(client)
            clientUpdatedSubject.onNext(client)
            Completable.complete()
        }
    }
}