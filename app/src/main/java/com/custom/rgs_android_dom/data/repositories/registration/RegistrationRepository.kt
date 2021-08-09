package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

interface RegistrationRepository {

    fun getCode(phone: String): Completable

    fun login(phone: String, code: String): Single<Boolean>

    fun acceptAgreement(): Single<Boolean>

    fun updateProfile(phone: String, name: String?, surname: String?, birthday: LocalDate?, gender: Gender?, agentCode: String?, agentPhone: String?): Single<Boolean>

    fun getAuthToken(): String?

    fun logout(): Completable

    fun getLogoutSubject(): BehaviorRelay<Unit>

}