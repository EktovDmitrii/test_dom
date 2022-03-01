package com.custom.rgs_android_dom.ui.property.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class DeletePropertyViewModel(
    private val property: PropertyItemModel,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private val propertyController = MutableLiveData<PropertyItemModel>()
    val propertyObserver: LiveData<PropertyItemModel> = propertyController

    init {
        propertyController.value = property
    }

    fun onDeleteClick(){
        propertyInteractor.deleteProperty(property.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.value = LoadingState.LOADING
            }
            .subscribeBy(
                onComplete = {
                    close()
                },
                onError = {
                    loadingStateController.value = LoadingState.ERROR
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}
