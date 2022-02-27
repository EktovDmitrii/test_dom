package com.custom.rgs_android_dom.ui.property.info

import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class EditPropertyAvatarBottomSheetViewModel(private val propertyInteractor: PropertyInteractor) : BaseViewModel() {

    fun onAvatarSelected(avatar: File) {
        propertyInteractor.updatePropertyAvatar(avatar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onDeleteAvatarClick(){
        propertyInteractor.removePropertyAvatar()
        closeController.value = Unit
    }


}