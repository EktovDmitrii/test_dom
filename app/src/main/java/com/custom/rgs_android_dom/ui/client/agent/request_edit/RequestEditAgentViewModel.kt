package com.custom.rgs_android_dom.ui.client.agent.request_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RequestEditAgentViewModel(
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val isSuccessTextViewVisibleController = MutableLiveData<Boolean>()
    val isSuccessTextViewVisibleObserver: LiveData<Boolean> = isSuccessTextViewVisibleController

    init {

    }

    fun onConfirmClick(){
        clientInteractor.requestEditAgent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    loadingStateController.value = LoadingState.CONTENT
                    isSuccessTextViewVisibleController.value = true
                },
                onError = {
                    logException(this, it)
                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCancelClick(){
        closeController.value = Unit
    }

    fun onSuccessClick(){
        closeController.value = Unit
    }

}