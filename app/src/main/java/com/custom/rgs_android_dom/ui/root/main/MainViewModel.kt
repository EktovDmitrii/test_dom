package com.custom.rgs_android_dom.ui.root.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    private val registrationController = MutableLiveData(false)
    val registrationObserver: LiveData<Boolean> = registrationController

    init {
        registrationController.value = registrationInteractor.isAuthorized()

        registrationInteractor.getLoginSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    registrationController.value = true
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    registrationController.value = false
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onSignInClick() {
        ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
    }

    fun onProfileClick() {
        ScreenManager.showBottomScreen(ClientFragment())
    }

}