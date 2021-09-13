package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.LocalDateTime

interface ClientRepository {

    fun updateClient(firstName: String?,
                     lastName: String?,
                     middleName: String?,
                     birthday: LocalDateTime?,
                     gender: Gender?,
                     agentCode: String?,
                     agentPhone: String?,
                     phone: String?,
                     email: String?): Completable

    fun getClient(): Single<ClientModel>

    fun loadAndSaveClient(): Completable

    fun getClientUpdatedSubject(): Observable<ClientModel>

    fun updateAgent(code: String, phone: String): Completable

    fun updatePassport(serial: String, number: String): Completable

    fun saveSecondPhone(phone: String): Completable

    fun updateSecondPhone(phone: String, id: String): Completable

    fun deleteContacts(ids: ArrayList<String>): Completable

}