package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.domain.countries.model.CountryModel
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.DateTime
import org.joda.time.LocalDate

interface RegistrationRepository {

    fun getCode(phone: String): Single<String>

    fun login(phone: String, code: String, token: String): Single<Boolean>

    fun signOpd(clientId: String): Completable

    fun updateProfile(phone: String, name: String?, surname: String?, birthday: LocalDate?, gender: Gender?, agentCode: String?, agentPhone: String?): Single<Boolean>

    fun getAuthToken(): String?

    fun logout(): Completable

    fun getLogoutSubject(): BehaviorRelay<Unit>

    fun getClientId(): String?

    fun refreshToken(refreshToken: String): Completable

    fun getRefreshTokenExpiresAt(): DateTime?

    fun deleteTokens()

    fun getRefreshToken(): String?
}