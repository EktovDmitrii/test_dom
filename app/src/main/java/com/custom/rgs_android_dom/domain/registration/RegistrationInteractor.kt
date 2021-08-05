package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.models.AuthModel
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

class RegistrationInteractor(private val registrationRepository: RegistrationRepository){

    fun requestCode(phone: String): Completable {
        return registrationRepository.requestCode(phone)
    }

    fun sendCode(phone: String, code: String): Single<AuthModel> {
        return registrationRepository.sendCode(phone, code)
    }

    fun saveAuth(auth: AuthModel){
        registrationRepository.saveAuth(auth)
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