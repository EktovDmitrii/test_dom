package com.custom.rgs_android_dom.ui.splash

import android.util.Log
import com.custom.rgs_android_dom.domain.TranslationInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.demo.DemoRegistrationFlowFragment
import com.custom.rgs_android_dom.ui.main.MainFragment
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.TranslationHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(private val registrationInteractor: RegistrationInteractor, private val translationInteractor: TranslationInteractor) : BaseViewModel() {

    companion object{
        private const val LOG = "SPLASH"
    }

    init {
        Log.d(LOG, "TRANSLATION TEST ${TranslationHelper.getTranslation("test.9pqfp11z4ibbubacxb7s61eiqo")}")
        loadSplash()
    }

    private fun loadSplash() {
        translationInteractor.getTranslation()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribe({
                TranslationHelper.setTranslations(it)
                Log.d(LOG, "TRANSLATION INIT COMPLITE")
                startNextCreen()
            }, {
                Log.d(LOG, "ERROR INIT TRANSLATION")
                startNextCreen()
            }).addTo(dataCompositeDisposable)
    }

    private fun startNextCreen(){
        closeController.value = Unit
        if (registrationInteractor.isAuthorized()){
            ScreenManager.showScreen(MainFragment())
        } else {
            ScreenManager.showScreen(DemoRegistrationFlowFragment())
        }
    }
}
