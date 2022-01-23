package com.custom.rgs_android_dom.ui.purchase_service

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase_service.PurchaseServiceModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address.PurchaseAddressFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PurchaseServiceViewModel(
    private val purchaseServiceModel: PurchaseServiceModel,
    private val propertyInteractor: PropertyInteractor
) : BaseViewModel() {

    private var propertyListSize: Int? = null

    private val purchaseServiceController = MutableLiveData<PurchaseServiceModel>()
    val purchaseServiceControllerObserver: LiveData<PurchaseServiceModel> =
        purchaseServiceController


    init {
        purchaseServiceController.postValue(purchaseServiceModel)

        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyListSize = it.size
                    updateAddress(it.last())
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun updateAddress(propertyItemModel: PropertyItemModel) {
        val oldValue = purchaseServiceController.value
        oldValue?.propertyItemModel = propertyItemModel
        oldValue?.let { purchaseServiceController.postValue(it) }
    }

    fun updateEmail(email: String) {
        val oldValue = purchaseServiceController.value
        oldValue?.email = email
        oldValue?.let { purchaseServiceController.postValue(it) }
    }

    fun updateComment(comment: String) {
        val oldValue = purchaseServiceController.value
        oldValue?.comment = comment
        oldValue?.let { purchaseServiceController.postValue(it) }
    }

    fun onAddressClick(childFragmentManager: FragmentManager) {
        val editDocumentBottomSheetFragment =
            PurchaseAddressFragment.newInstance(purchaseServiceController.value?.propertyItemModel)
        editDocumentBottomSheetFragment.show(
            childFragmentManager,
            editDocumentBottomSheetFragment.TAG
        )
    }

    fun onAddPropertyClick() {
        ScreenManager.showScreenScope(
            SelectAddressFragment.newInstance(propertyListSize ?: 0),
            ADD_PROPERTY
        )
    }
}
