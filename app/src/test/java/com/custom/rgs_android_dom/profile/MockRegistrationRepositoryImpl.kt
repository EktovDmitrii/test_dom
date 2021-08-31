package com.custom.rgs_android_dom.profile


import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleMap
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import java.util.*

class MockRegistrationRepositoryImpl : RegistrationRepository {

    private var phone: String = "78888888888"
    private var currentToken: String? = null
    private var currentRefreshToken: String? = null
    private var currentClientId: String? = null
    private val logout = PublishSubject.create<Unit>()

    override fun getCurrentPhone(): String {
        return phone
    }

    override fun getCode(code: String): Single<String> {
        return Single.fromCallable{
            Thread.sleep(4000)
            if (code.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                "1111"
            }
        }
    }

    override fun login(phone: String, code: String, token: String): Single<Boolean> {
        return SingleMap.just(true)
    }

    override fun signOpd(clientId: String): Completable {
        return Completable.complete()
    }

    override fun getAuthToken(): String? {
        return currentToken
    }

    override fun logout(): Completable {
        currentToken = null
        currentRefreshToken = null
        return Completable.complete()
    }

    override fun getLogoutSubject(): PublishSubject<Unit> {
        return logout
    }

    override fun getClientId(): String? {
        return currentClientId
    }

    override fun refreshToken(refreshToken: String): Completable {
        currentRefreshToken = refreshToken
        return Completable.complete()
    }

    override fun getRefreshTokenExpiresAt(): DateTime? {
        return DateTime.now()
    }

    override fun deleteTokens() {
        currentToken = null
        currentRefreshToken = null
    }

    override fun getRefreshToken(): String? {
        return currentRefreshToken
    }

    override fun isAuthorized(): Boolean {
        return currentClientId != null
    }

    override fun clearAuth() {
        currentRefreshToken = null
        currentToken = null
        currentClientId = null
        logout.onNext(Unit)
    }

}