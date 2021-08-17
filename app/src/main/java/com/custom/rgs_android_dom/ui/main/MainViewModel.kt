package com.custom.rgs_android_dom.ui.main

import android.util.Log
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.demo.DemoRegistrationFlowFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val registrationInteractor: RegistrationInteractor) : BaseViewModel(){

    init {
        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    Log.d("MyLog", "MAIN FRAGMENT ON CLOSER")
                    closeController.value = Unit
                    ScreenManager.showScreen(DemoRegistrationFlowFragment())
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

    }


}