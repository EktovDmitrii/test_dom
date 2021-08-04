package com.custom.rgs_android_dom.data.repositories.registration

import com.custom.rgs_android_dom.data.network.MyServiceDomApi
import com.custom.rgs_android_dom.domain.profile.models.Gender
import io.reactivex.Single
import org.joda.time.LocalDate
import java.util.*

class MockRegistrationRepositoryImpl(private val api: MyServiceDomApi) : RegistrationRepository {

    override fun sendPhone(phone: String): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            if (phone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun sendCode(code: String): Single<Boolean> {
        return Single.fromCallable{
            Thread.sleep(4000)
            if (code.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun resendCode(phone: String): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            if (phone.endsWith("9")){
                throw InvalidPropertiesFormatException("Wrong format")
            } else {
                true
            }
        }
    }

    override fun acceptAgreement(): Single<Boolean> {
        return Single.fromCallable {
            Thread.sleep(2000)
            true
        }
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

}