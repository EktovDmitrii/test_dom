package com.custom.rgs_android_dom.domain.registration

import com.custom.rgs_android_dom.data.providers.auth.manager.AuthState
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
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

    fun logout(nextScreen: TargetScreen? = null): Completable {
        return registrationRepository.logout(nextScreen)
    }

    fun getLogoutSubject(): PublishSubject<TargetScreen>{
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

    fun isFirstRun(): Boolean {
        return registrationRepository.isFirstRun()
    }

    fun getAuthFlowEndedSubject(): PublishSubject<Unit>{
        return registrationRepository.getAuthFlowEndedSubject()
    }

    fun deleteClient(): Completable {
        return registrationRepository.deleteClient()
    }

}