package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import io.reactivex.Single

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

}