package com.custom.rgs_android_dom.ui.splash

import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel : BaseViewModel() {

    init {
        loadSplash()
    }

    private fun loadSplash() {
        Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribe({
                loadingStateObserver.value = LoadingState.CONTENT
            }, {
                loadingStateObserver.value = LoadingState.ERROR
            }).addTo(dataCompositeDisposable)
    }
}
