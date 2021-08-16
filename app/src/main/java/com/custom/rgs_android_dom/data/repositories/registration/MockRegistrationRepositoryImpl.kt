package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleMap
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.util.*

class MockRegistrationRepositoryImpl : RegistrationRepository {

    companion object{
        val MOCK_TOKEN = "MOCK_TOKEN"
    }

    private var phone: String = "78888888888"
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
        return MOCK_TOKEN
    }

    override fun logout(): Completable {
        TODO("Not yet implemented")
    }

    override fun getLogoutSubject(): BehaviorRelay<Unit> {
        TODO("Not yet implemented")
    }

    override fun getClientId(): String? {
        TODO("Not yet implemented")
    }

    override fun refreshToken(refreshToken: String): Completable {
        TODO("Not yet implemented")
    }

    override fun getRefreshTokenExpiresAt(): DateTime? {
        TODO("Not yet implemented")
    }

    override fun deleteTokens() {
        TODO("Not yet implemented")
    }

    override fun getRefreshToken(): String? {
        TODO("Not yet implemented")
    }

}