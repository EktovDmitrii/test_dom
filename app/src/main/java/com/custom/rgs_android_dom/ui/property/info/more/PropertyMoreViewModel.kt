package com.custom.rgs_android_dom.ui.property.info.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.OrderStatus
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.UPDATE_PROPERTY
import com.custom.rgs_android_dom.ui.property.info.edit.EditPropertyInfoFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PropertyMoreViewModel(
    private val property: PropertyItemModel,
    private val clientInteractor: ClientInteractor,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val deletePropertyController = MutableLiveData<PropertyItemModel>()
    val deletePropertyObserver: LiveData<PropertyItemModel> = deletePropertyController

    fun onEditPropertyClick() {
        Single.zip(
            clientInteractor.getOrdersHistory(),
            catalogInteractor.getAvailableServices(),
            catalogInteractor.getProductsOnBalance(),
            catalogInteractor.getProductsByContracts()
        ) { orders, services, balanceProducts, contractProducts ->
            val hasActiveOrders = orders.firstOrNull { it.objectId == property.id && (it.status == OrderStatus.ACTIVE || it.status == OrderStatus.CONFIRMED) } != null
            val hasProducts = contractProducts.firstOrNull { it.objectId == property.id } != null
            val hasBalanceProducts = balanceProducts.firstOrNull { it.objectId == property.id } != null
            val hasServices = services.firstOrNull { it.objectId == property.id } != null
            !(hasActiveOrders || hasProducts || hasServices || hasBalanceProducts)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { isEditable ->
                    close()
                    ScreenManager.showScreenScope(
                        EditPropertyInfoFragment.newInstance(property.id, isEditable),
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