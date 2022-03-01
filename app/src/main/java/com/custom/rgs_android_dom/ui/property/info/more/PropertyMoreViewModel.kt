package com.custom.rgs_android_dom.ui.property.info.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.UPDATE_PROPERTY
import com.custom.rgs_android_dom.ui.property.info.edit.EditPropertyInfoFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PropertyMoreViewModel(
    private val property: PropertyItemModel,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val deletePropertyController = MutableLiveData<PropertyItemModel>()
    val deletePropertyObserver: LiveData<PropertyItemModel> = deletePropertyController

    fun onEditPropertyClick(){
        clientInteractor.getOrdersHistory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { orders ->
                    close()
                    val isObjectEditable = orders.firstOrNull { it.objectId == property.id && (it.status == OrderStatus.ACTIVE || it.status == OrderStatus.CONFIRMED) } == null
                    ScreenManager.showScreenScope(
                        EditPropertyInfoFragment.newInstance(property.id, isObjectEditable),
                        UPDATE_PROPERTY
                    )
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onDeletePropertyClick(){
        close()
        deletePropertyController.value = property
    }

}