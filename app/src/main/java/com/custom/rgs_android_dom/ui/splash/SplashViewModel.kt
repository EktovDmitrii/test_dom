package com.custom.rgs_android_dom.ui.splash

import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.root.RootFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(private val registrationInteractor: RegistrationInteractor, private val clientInteractor: ClientInteractor) : BaseViewModel() {

    init {
        loadSplash()
    }

    private fun loadSplash() {
        Observable.timer(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribe({
                checkAuth()
            }, {
                checkAuth()
            })
            .addTo(dataCompositeDisposable)
    }


    private fun checkAuth(){
        if (registrationInteractor.isAuthorized()){
            clientInteractor.loadAndSaveClient()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onComplete = {
                        showMainScreen()
                    },
                    onError = {
                        logException(this, it)
                        showMainScreen()
                    }
                ).addTo(dataCompositeDisposable)

        } else {
            showMainScreen()
        }
    }

    private fun showMainScreen(){
        ScreenManager.showScreen(RootFragment())
        closeController.value = Unit
    }
}
