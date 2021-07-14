package com.custom.rgs_android_dom.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel() : ViewModel(){
    protected val dataCompositeDisposable = CompositeDisposable()

    protected val loadingStateController = MutableLiveData<LoadingState>()
    val loadingStateObserver = loadingStateController

    override fun onCleared() {
        dataCompositeDisposable.clear()
        super.onCleared()
    }

    enum class LoadingState{
        LOADING,
        CONTENT,
        ERROR
    }
}