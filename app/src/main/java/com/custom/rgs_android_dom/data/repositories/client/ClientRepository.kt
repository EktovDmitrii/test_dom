package com.custom.rgs_android_dom.data.repositories.client

import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDateTime

interface ClientRepository {

    fun updateClient(firstName: String?, lastName: String?, birthday: LocalDateTime?, gender: Gender?, agentCode: String?, agentPhone: String?): Completable

    fun getClientFromCache(): Single<ClientModel>

    fun loadClient(): Completable

    fun getClientUpdatedSubject(): BehaviorRelay<ClientModel>

}