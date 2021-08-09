package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

class RegistrationInteractor(private val registrationRepository: RegistrationRepository){

    fun getCode(phone: String): Completable {
        return registrationRepository.getCode(phone)
    }

    fun login(phone: String, code: String): Single<Boolean> {
        return registrationRepository.login(phone, code)
    }

    fun acceptAgreement(): Single<Boolean> {
        return registrationRepository.acceptAgreement()
    }

    fun logout(): Completable {
        return registrationRepository.logout()
    }

    fun getLogoutSubject(): BehaviorRelay<Unit>{
        return registrationRepository.getLogoutSubject()
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