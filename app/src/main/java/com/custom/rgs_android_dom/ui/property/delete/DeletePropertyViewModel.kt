package com.custom.rgs_android_dom.ui.property.delete

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DeletePropertyViewModel(
    private val property: PropertyItemModel,
    private val propertyInteractor: PropertyInteractor,
    private val clientInteractor: ClientInteractor,
    private val chatInteractor: ChatInteractor,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val propertyController = MutableLiveData<PropertyItemModel>()
    val propertyObserver: LiveData<PropertyItemModel> = propertyController

    private val cannotBeDeletedController = MutableLiveData<Unit>()
    val cannotBeDeletedObserver: LiveData<Unit> = cannotBeDeletedController

    init {
        propertyController.value = property
    }

    fun onDeleteClick(){
        Single.zip(clientInteractor.getOrdersHistory(), catalogInteractor.getProductsByContracts()){orders, products->
            val hasActiveOrders = orders.firstOrNull { it.status == OrderStatus.ACTIVE && it.objectId == property.id } != null
            val hasProducts = products.firstOrNull { it.objectId == property.id } != null
            hasActiveOrders || hasProducts
        }
        .doOnSubscribe {
            loadingStateController.postValue(LoadingState.LOADING)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onSuccess = {
                if (it){
                    cannotBeDeletedController.value = Unit
                } else {
                    deleteProperty()
                }
            },
            onError = {
                logException(this, it)
                loadingStateController.value = LoadingState.ERROR
            }
        ).addTo(dataCompositeDisposable)
    }

    fun onContactMasterOnlineClick(){
        close()
        ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
    }

    private fun deleteProperty(){
        propertyInteractor.deleteProperty(property.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
