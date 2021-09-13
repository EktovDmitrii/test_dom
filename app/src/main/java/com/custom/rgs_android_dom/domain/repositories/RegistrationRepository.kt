package com.custom.rgs_android_dom.domain.repositories

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime

interface RegistrationRepository {

    fun getCurrentPhone(): String

    fun getCode(phone: String): Single<String>

    fun login(phone: String, code: String, token: String): Single<Boolean>

    fun signOpd(clientId: String): Completable

    fun getAuthToken(): String?

    fun logout(): Completable

    fun getLogoutSubject(): PublishSubject<Unit>

    fun getClientId(): String?

    fun refreshToken(refreshToken: String): Completable

    fun getRefreshTokenExpiresAt(): DateTime?

    fun deleteTokens()

    fun getRefreshToken(): String?

    fun isAuthorized(): Boolean

    fun clearAuth()

    fun setMockToken()

}