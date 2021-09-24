package com.custom.rgs_android_dom.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.component.KoinComponent

open class BaseViewModel : ViewModel(), KoinComponent {
    protected val dataCompositeDisposable = CompositeDisposable()

    protected val loadingStateController = MutableLiveData<LoadingState>()
    val loadingStateObserver: LiveData<LoadingState> = loadingStateController

    protected val closeController = MutableLiveData<Unit>()
    val closeObserver: LiveData<Unit> = closeController

    protected val networkErrorController = MutableLiveData<String>()
    val networkErrorObserver: LiveData<String> = networkErrorController

    fun close(){
        closeController.value = Unit
    }

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