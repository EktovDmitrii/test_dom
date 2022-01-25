package com.custom.rgs_android_dom.ui.purchase_service

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseDateTimeModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseServiceModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.property.add.select_address.SelectAddressFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase_service.edit_purchase_service_address.PurchaseAddressFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PurchaseViewModel(
    private val serviceModel: PurchaseServiceModel,
    private val propertyInteractor: PropertyInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private var propertyListSize: Int? = null

    private val purchaseServiceController = MutableLiveData<PurchaseServiceModel>()
    val purchaseServiceObserver: LiveData<PurchaseServiceModel> = purchaseServiceController

    init {
        purchaseServiceController.value = serviceModel

        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyListSize = it.size
                    if (it.isNotEmpty()) updateAddress(it.last())
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun updateAddress(propertyItemModel: PropertyItemModel) {
        val newValue = purchaseServiceController.value
        newValue?.propertyItemModel = propertyItemModel
        newValue?.let {
            purchaseServiceController.value = it
        }
    }

    fun updateEmail(email: String) {
        val oldValue = purchaseServiceController.value
        oldValue?.email = email
        oldValue?.let {
            purchaseServiceController.value = it
        }
    }


    fun updateAgentCode(code: String) {
        val oldValue = purchaseServiceController.value
        oldValue?.agentCode = code
        oldValue?.let {
            purchaseServiceController.value = it
        }
    }

    fun updateCard(card: String) {
        val oldValue = purchaseServiceController.value
        oldValue?.card = card
        oldValue?.let {
            purchaseServiceController.value = it
        }
    }

    fun updateComment(comment: String) {
        val newValue = purchaseServiceController.value
        newValue?.comment = comment
        newValue?.let { purchaseServiceController.postValue(it) }
    }

    fun updateDateTime(purchaseDateTimeModel: PurchaseDateTimeModel) {
        val newValue = purchaseServiceController.value
        newValue?.purchaseDateTimeModel = purchaseDateTimeModel
        newValue?.let { purchaseServiceController.postValue(it) }
    }

    fun onAddressClick(childFragmentManager: FragmentManager) {
        val purchaseAddressFragment =
            PurchaseAddressFragment.newInstance(purchaseServiceController.value?.propertyItemModel)
        purchaseAddressFragment.show(
            childFragmentManager,
            purchaseAddressFragment.TAG
        )
    }

    fun onDateTimeClick(childFragmentManager: FragmentManager) {
        val purchaseDateTimeFragment = PurchaseDateTimeFragment.newInstance(
            purchaseServiceController.value?.purchaseDateTimeModel
        )
        purchaseDateTimeFragment.show(childFragmentManager, purchaseDateTimeFragment.TAG)
    }

    fun onCardClick(childFragmentManager: FragmentManager) {
        val cardFragment = SelectCardBottomFragment.newInstance()
        cardFragment.show(childFragmentManager, cardFragment.TAG)
    }

    fun onAddPropertyClick() {
        ScreenManager.showScreenScope(
            SelectAddressFragment.newInstance(propertyListSize ?: 0),
            ADD_PROPERTY
        )
    }

    fun makeOrder() {
        purchaseServiceObserver.value?.let {
            purchaseInteractor.makeProductPurchase(
                productId = it.id,
                bindingId = null,
                email = "pav.develop@yandex.ru",
                objectId = "19693951-05ea-41c5-86f2-ea1e3f888e4d",
                saveCard = true,
                deliveryDate = "2022-02-25T00:00:00+04:00",
                timeFrom = "10:00",
                timeTo = "10:00"
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                                Log.d("MyLogs", it)
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }
}
