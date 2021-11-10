package com.custom.rgs_android_dom.ui.photo.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class AddPhotoViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val isDeleteTextViewVisibleController = MutableLiveData<Boolean>()
    val isDeleteTextViewVisibleObserver: LiveData<Boolean> = isDeleteTextViewVisibleController

    init {
        clientInteractor.getClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isDeleteTextViewVisibleController.value = it.avatar?.isNotEmpty() == true
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onAvatarSelected(avatar: File) {
        clientInteractor.updateAvatar(avatar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {

                },
                onError = {

                }
            ).addTo(dataCompositeDisposable)
    }

}