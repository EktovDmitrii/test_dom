package com.custom.rgs_android_dom.ui.demo

import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.DemoInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DemoViewModel: BaseViewModel() {

    private val demoTextController = MutableLiveData<String>()
    val demoTextObserver = demoTextController

    private val demoInteractor: DemoInteractor by inject()

    init {
        loadData()
    }

    private fun loadData(){
        demoInteractor.getDemoModel()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .subscribeBy(
                onSuccess = {
                    demoTextController.value = it.text
                },
                onError = {
                    //todo обработка ошибки
                }
            ).addTo(dataCompositeDisposable)
    }
}