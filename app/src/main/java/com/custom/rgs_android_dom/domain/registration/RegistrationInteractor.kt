package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import io.reactivex.Single
import org.joda.time.LocalDate

class RegistrationInteractor(private val registrationRepository: RegistrationRepository){

    fun sendPhone(phone: String): Single<Boolean> {
        return registrationRepository.sendPhone(phone)
    }

    fun sendCode(code: String): Single<Boolean> {
        return registrationRepository.sendCode(code)
    }

    fun resendCode(phone: String) : Single<Boolean> {
        return registrationRepository.resendCode(phone)
    }

    fun acceptAgreement(): Single<Boolean> {
        return registrationRepository.acceptAgreement()
    }

    fun updateProfile(
        phone: String,
        name: String?,
        surname: String?,
        birthday: LocalDate?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Single<Boolean> {
        return registrationRepository.updateProfile(phone, name, surname, birthday, gender, agentCode, agentPhone)
    }

}