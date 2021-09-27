package com.custom.rgs_android_dom.ui.splash

import android.util.Log
import com.custom.rgs_android_dom.domain.TranslationInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.demo.DemoRegistrationFlowFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class SplashViewModel(private val registrationInteractor: RegistrationInteractor, private val translationInteractor: TranslationInteractor) : BaseViewModel() {

    companion object{
        private const val LOG = "SPLASH"
    }

    init {
        loadSplash()
    }

    private fun loadSplash() {

        Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribe({
                startNextScreen()
            }, {
                startNextScreen()
            }).addTo(dataCompositeDisposable)
    }

    private fun startNextScreen(){
        closeController.value = Unit
        if (registrationInteractor.isAuthorized()){
            ScreenManager.showScreen(MainFragment())
        } else {
            ScreenManager.showScreen(DemoRegistrationFlowFragment())
        }
    }
}
