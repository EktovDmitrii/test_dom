package com.custom.rgs_android_dom.ui.property.info.edit.request_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RequestPropertyInfoEditViewModel(
    private val objectId: String,
    private val propertyInteractor: PropertyInteractor
    ) : BaseViewModel() {

    private val isSuccessTextViewVisibleController = MutableLiveData<Boolean>()
    val isSuccessTextViewVisibleObserver: LiveData<Boolean> = isSuccessTextViewVisibleController

    fun onConfirmClick(){
        propertyInteractor.requestModification(objectId)
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