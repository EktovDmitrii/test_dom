package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.providers.auth.manager.AuthState
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
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

    fun signOpd(): Completable {
        return registrationRepository.signOpd()
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

    fun getAuthStateSubject(): PublishSubject<AuthState> {
        return registrationRepository.getAuthStateSubject()
    }

    fun forceSaveAuthCredentials(){
        registrationRepository.forceSaveAuthCredentials()
    }

    fun getLoginSubject(): PublishSubject<Unit>{
        return registrationRepository.getLoginSubject()
    }

}