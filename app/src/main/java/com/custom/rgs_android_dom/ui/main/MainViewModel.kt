package com.custom.rgs_android_dom.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.responses.TokenResponse
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthState
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.MainCatalogFragment
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.main.stub.MainStubFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.agreement.RegistrationAgreementFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.koin.core.component.inject

class MainViewModel(private val registrationInteractor: RegistrationInteractor,
                    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val isProfileLayoutVisibleController = MutableLiveData<Boolean>()
    val isProfileLayoutVisibleObserver: LiveData<Boolean> = isProfileLayoutVisibleController

    private val authContentProviderManager: AuthContentProviderManager by inject()
    private val logoutCompositeDisposable = CompositeDisposable()

    init {
        checkSignedAgreement()
        subscribeAuthStateChanges()

        isProfileLayoutVisibleController.value = registrationInteractor.isAuthorized()

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    isProfileLayoutVisibleController.value = true
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun subscribeLogout() {
        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    /*ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
                    closeController.value = Unit*/
                    isProfileLayoutVisibleController.value = false
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(logoutCompositeDisposable)
    }


    fun unsubscribeLogout() {
        //logoutCompositeDisposable.clear()
    }

    fun onChatClick() {
        if (registrationInteractor.isAuthorized()){
            ScreenManager.showScreen(ChatFragment())
        }
    }

    fun onMainClick(){
        ScreenManager.showBottomScreen(MainStubFragment())
    }

    fun onCatalogueClick(){
        ScreenManager.showBottomScreen(MainCatalogFragment())
    }

    fun onProfileClick(){
        ScreenManager.showBottomScreen(ClientFragment())
    }

    fun onLoginClick(){
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
    }

    private fun checkSignedAgreement(){
        if (registrationInteractor.isAuthorized()){
            clientInteractor.getClient()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        if (!it.isOpdSigned){
                            ScreenManager.showScreenScope(RegistrationAgreementFragment.newInstance(it.phone, true), REGISTRATION)
                        }
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }

    private fun subscribeAuthStateChanges(){
        registrationInteractor.getAuthStateSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it){
                        AuthState.AUTH_CLEARED -> {
                            logout()
                        }
                        AuthState.AUTH_SAVED -> {
                            logoutAndReloadClient()
                        }
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun logoutAndReloadClient(){
        registrationInteractor.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen(clientInteractor.loadAndSaveClient())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(dataCompositeDisposable)
    }

    private fun logout() {
        registrationInteractor.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .addTo(dataCompositeDisposable)
    }

}