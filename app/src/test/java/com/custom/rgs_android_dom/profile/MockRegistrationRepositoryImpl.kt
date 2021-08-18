package com.custom.rgs_android_dom.profile


import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleMap
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class MockRegistrationRepositoryImpl : RegistrationRepository {

    private var phone: String = "78888888888"
    private var currentToken: String? = null
    private var currentRefreshToken: String? = null
    private var currentClientId: String? = null

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

    override fun updateProfile(
        name: String?,
        surname: String?,
        birthday: LocalDate?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(3000)
            val agentPhone = agentPhone
            if (agentPhone != null && agentPhone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun getAuthToken(): String? {
        return currentToken
    }

    override fun logout(): Completable {
        currentToken = null
        currentRefreshToken = null
        return Completable.complete()
    }

    override fun getLogoutSubject(): BehaviorRelay<Unit> {
        TODO("Not yet implemented")
    }

    override fun getClientId(): String? {
        return currentClientId
    }

    override fun refreshToken(refreshToken: String): Completable {
        currentRefreshToken = refreshToken
        return Completable.complete()
    }

    override fun getRefreshTokenExpiresAt(): DateTime? {
        TODO("Not yet implemented")
    }

    override fun deleteTokens() {
        currentToken = null
        currentRefreshToken = null
    }

    override fun getRefreshToken(): String? {
        return currentRefreshToken
    }

}