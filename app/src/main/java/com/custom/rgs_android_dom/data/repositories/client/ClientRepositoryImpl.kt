package com.custom.rgs_android_dom.data.repositories.client

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
import org.joda.time.LocalDateTime

class ClientRepositoryImpl(
    private val api: MSDApi,
    private val authSharedPreferences: AuthSharedPreferences
) : ClientRepository {

    private val clientUpdatedSubject: BehaviorRelay<ClientModel> = BehaviorRelay.create()

    override fun updateClient(
        firstName: String?,
        lastName: String?,
        middleName: String?,
        birthday: LocalDateTime?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?,
        docNumber: String?,
        docSerial: String?,
        phone: String?,
        secondPhone: String?,
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
            docNumber = docNumber,
            docSerial = docSerial,
            phone = phone,
            secondPhone = secondPhone?.formatPhoneForApi(),
            email = email
        )
        return api.putClient(authSharedPreferences.getClientId(), request)
            .flatMapCompletable { response ->
                val client = ClientMapper.responseToClient(response)
                authSharedPreferences.saveClient(client)
                clientUpdatedSubject.accept(client)
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

            authSharedPreferences.getClient()?.let { clientCached ->
                if (clientCached != client) {
                    authSharedPreferences.saveClient(client)
                    clientUpdatedSubject.accept(client)
                }
            }

            Completable.complete()
        }
    }

    override fun getClientUpdatedSubject(): Observable<ClientModel> {
        return clientUpdatedSubject.hide()
    }
}