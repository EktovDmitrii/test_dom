package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.data.providers.auth.manager.AuthState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime

interface RegistrationRepository {

    fun getCurrentPhone(): String

    fun getCode(phone: String): Single<String>

    fun login(phone: String, code: String, token: String): Single<Boolean>

    fun signOpd(): Completable

    fun getAccessToken(): String?

    fun logout(): Completable

    fun getLogoutSubject(): PublishSubject<Unit>

    fun refreshToken(refreshToken: String): Completable

    fun getRefreshTokenExpiresAt(): DateTime?

    fun deleteTokens()

    fun getRefreshToken(): String?

    fun isAuthorized(): Boolean

    fun clearAuth()

    fun getAuthStateSubject(): PublishSubject<AuthState>

    fun forceSaveAuthCredentials()

    fun getLoginSubject(): PublishSubject<Unit>

    fun isFirstRun(): Boolean

    fun getAuthFlowEndedSubject(): PublishSubject<Unit>

    fun finishAuth()
}