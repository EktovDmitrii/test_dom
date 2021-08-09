package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate
import java.util.*

class MockRegistrationRepositoryImpl(private val api: MSDApi) : RegistrationRepository {

    private val logout = BehaviorRelay.create<Unit>()

    override fun login(phone: String, code: String): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            if (phone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
     }

    override fun getCode(phone: String): Completable {
        Thread.sleep(2000)
        return if (phone.endsWith("9")){
            throw InvalidPropertiesFormatException("Wrong format")
        } else {
            Completable.complete()
        }
    }

   override fun acceptAgreement(): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            true
        }
    }

    override fun getAuthToken(): String? {
        return ""
    }

    override fun updateProfile(
        phone: String,
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

    override fun logout(): Completable {
        return Completable.fromCallable {
            logout.accept(Unit)
        }
        //
    }

    override fun getLogoutSubject(): BehaviorRelay<Unit> {
        return logout
    }
}