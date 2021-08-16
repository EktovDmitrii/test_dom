package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

class RegistrationInteractor(private val registrationRepository: RegistrationRepository){

    fun getCode(phone: String): Single<String> {
        return registrationRepository.getCode(phone)
    }

    fun login(phone: String, code: String, token: String): Single<Boolean> {
        return registrationRepository.login(phone, code, token)
    }

    fun signOpd(clientId: String): Completable {
        return registrationRepository.signOpd(clientId)
    }

    fun getClientId(): String? {
        return registrationRepository.getClientId()
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