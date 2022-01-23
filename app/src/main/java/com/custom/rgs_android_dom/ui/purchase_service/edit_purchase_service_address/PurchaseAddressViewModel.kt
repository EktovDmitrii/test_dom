package com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PurchaseAddressViewModel(
   selectedPropertyItem: PropertyItemModel,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    var selectedPropertyItemModel: PropertyItemModel? = null

    private val propertyController =
        MutableLiveData<List<PropertyItemModel>>()
    val propertyObserver: LiveData<List<PropertyItemModel>> =
        propertyController

    init {
        selectedPropertyItemModel = selectedPropertyItem

        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyController.value = it
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }
}
