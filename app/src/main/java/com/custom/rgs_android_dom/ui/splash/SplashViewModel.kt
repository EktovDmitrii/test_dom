package com.custom.rgs_android_dom.ui.splash

import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
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
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }
}
