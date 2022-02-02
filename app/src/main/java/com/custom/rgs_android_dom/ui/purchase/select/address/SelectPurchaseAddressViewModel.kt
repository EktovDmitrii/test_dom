package com.custom.rgs_android_dom.ui.purchase.select.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectPurchaseAddressViewModel(
    private var selectedPropertyItem: PropertyItemModel?,
    propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private val propertyController = MutableLiveData<Pair<PropertyItemModel?, List<PropertyItemModel>>>()
    val propertyObserver: LiveData<Pair<PropertyItemModel?, List<PropertyItemModel>>> = propertyController

    private var propertyCount: Int? = null

    init {
        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyController.value = Pair(selectedPropertyItem, it)
                    propertyCount = it.size
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }


    fun onAddPropertyClick() {
        ScreenManager.showScreenScope(
            SelectAddressFragment.newInstance(propertyCount ?: 0),
            ADD_PROPERTY
        )
    }
}
