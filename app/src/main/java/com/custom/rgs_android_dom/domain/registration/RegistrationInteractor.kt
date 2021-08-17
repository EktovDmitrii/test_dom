package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

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

    fun getLogoutSubject(): PublishSubject<Unit>{
        return registrationRepository.getLogoutSubject()
    }

    fun isAuthorized(): Boolean {
        return registrationRepository.isAuthorized()
    }

}