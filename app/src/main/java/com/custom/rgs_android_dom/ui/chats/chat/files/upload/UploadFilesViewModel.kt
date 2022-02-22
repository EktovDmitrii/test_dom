package com.custom.rgs_android_dom.ui.chats.chat.files.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class UploadFilesViewModel(private val chatInteractor: ChatInteractor) : BaseViewModel() {

    private val showInvalidUploadFileMessageController = MutableLiveData<File>()
    val showInvalidUploadFileMessageObserver: LiveData<File> = showInvalidUploadFileMessageController

    init {
        chatInteractor.invalidUploadFileSubject
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

        chatInteractor.getFilesToUploadSubject()
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
        chatInteractor.onFilesToUploadSelected(files)
    }

}