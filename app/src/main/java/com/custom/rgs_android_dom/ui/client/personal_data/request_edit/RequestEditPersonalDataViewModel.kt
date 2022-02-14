package com.custom.rgs_android_dom.ui.client.personal_data.request_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RequestEditPersonalDataViewModel(
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val isSuccessTextViewVisibleController = MutableLiveData<Boolean>()
    val isSuccessTextViewVisibleObserver: LiveData<Boolean> = isSuccessTextViewVisibleController

    fun onConfirmClick(){
        clientInteractor.requestEditPersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    YandexMetrica.reportEvent("profile_personal_data_request_confirm")

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