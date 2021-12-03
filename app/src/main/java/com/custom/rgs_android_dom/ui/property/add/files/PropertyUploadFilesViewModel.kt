package com.custom.rgs_android_dom.ui.property.add.files

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class PropertyUploadFilesViewModel(private val propertyInteractor: PropertyInteractor) : BaseViewModel() {

    private val showInvalidUploadFileMessageController = MutableLiveData<File>()
    val showInvalidUploadFileMessageObserver: LiveData<File> = showInvalidUploadFileMessageController

    /*init {
        propertyInteractor.invalidUploadFileSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    showInvalidUploadFileMessageController.value = it
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.getFilesToUploadSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onFilesSelected(files: List<File>){
        propertyInteractor.onFilesToUploadSelected(files)
    }*/

}