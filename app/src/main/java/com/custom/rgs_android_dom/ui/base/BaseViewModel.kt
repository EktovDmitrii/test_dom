package com.custom.rgs_android_dom.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.custom.rgs_android_dom.data.network.toMSDErrorModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
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

    protected val notificationController = MutableLiveData<String>()
    val notificationObserver: LiveData<String> = notificationController

    private val isKeyboardOpenController = MutableLiveData(false)
    val isKeyboardOpenObserver: LiveData<Boolean> = isKeyboardOpenController

    fun close(){
        closeController.value = Unit
    }

    fun onKeyboardOpen() {
        isKeyboardOpenController.value = true
    }

    fun onKeyboardClose() {
        isKeyboardOpenController.value = false
    }

    protected fun handleNetworkException(throwable: Throwable){
        throwable.toMSDErrorModel()?.let {
            networkErrorController.value = TranslationInteractor.getErrorTranslation(it)
        }
    }

    protected fun getNetworkExceptionMessage(throwable: Throwable): String? {
        throwable.toMSDErrorModel()?.let {
            return TranslationInteractor.getErrorTranslation(it)
        }
        return null
    }

    override fun onCleared() {
        disposeAll()
        super.onCleared()
    }

    fun disposeAll() {
        if (!dataCompositeDisposable.isDisposed) {
            dataCompositeDisposable.clear()
        }
    }

    enum class LoadingState{
        LOADING,
        CONTENT,
        ERROR
    }
}
