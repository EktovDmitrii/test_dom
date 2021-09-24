package com.custom.rgs_android_dom.ui.main

import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    private val logoutCompositeDisposable = CompositeDisposable()

    init {
        //registrationInteractor.saveMockToken()
    }

    fun subscribeLogout(){
        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    closeController.value = Unit
                    ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(logoutCompositeDisposable)
    }


    fun unsubscribeLogout(){
        logoutCompositeDisposable.clear()
    }

    fun onChatClick(){
        closeController.value = Unit
        ScreenManager.showScreen(ChatFragment())
    }
}