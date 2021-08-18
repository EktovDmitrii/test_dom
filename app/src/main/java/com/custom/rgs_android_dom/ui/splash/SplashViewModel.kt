package com.custom.rgs_android_dom.ui.splash

import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.demo.DemoRegistrationFlowFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(private val registrationInteractor: RegistrationInteractor) : BaseViewModel() {

    init {
        loadSplash()
    }

    private fun loadSplash() {
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribe({
                loadingStateController.value = LoadingState.CONTENT
                closeController.value = Unit
                if (registrationInteractor.isAuthorized()){
                    ScreenManager.showScreen(MainFragment())
                } else {
                    ScreenManager.showScreen(DemoRegistrationFlowFragment())
                }
            }, {
                loadingStateController.value = LoadingState.ERROR
            }).addTo(dataCompositeDisposable)
    }
}
