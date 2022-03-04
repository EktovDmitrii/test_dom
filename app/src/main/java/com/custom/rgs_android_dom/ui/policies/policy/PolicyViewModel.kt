package com.custom.rgs_android_dom.ui.policies.policy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.models.ServiceShortModel
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceFragment
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceLauncher
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderLauncher

import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PolicyViewModel(contractId: String, policiesInteractor: PoliciesInteractor) : BaseViewModel() {

    private val productController = MutableLiveData<PolicyModel>()
    val productObserver: LiveData<PolicyModel> = productController

    init {
        policiesInteractor.getClientProductSingle(contractId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { logException(this, it) },
                onSuccess = { productController.value = it }
            )
            .addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        close()
    }

    fun onServiceClick(serviceShortModel: ServiceShortModel,product: ProductLauncher) {
        val serviceFragment = ServiceFragment.newInstance(
            ServiceLauncher(
                productId = product.productId,
                serviceId = serviceShortModel.serviceId,
                isPurchased = product.isPurchased,
                purchaseValidTo = product.purchaseValidTo,
                purchaseObjectId = product.purchaseObjectId,
                quantity = serviceShortModel.quantity
            )
        )
        ScreenManager.showBottomScreen(serviceFragment)
    }

    fun onServiceOrderClick(serviceShortModel: ServiceShortModel,product: ProductLauncher){
        val serviceOrderFragment = ServiceOrderFragment.newInstance(
            ServiceOrderLauncher(
                serviceId = serviceShortModel.serviceId,
                productId = product.productId,
                deliveryType = serviceShortModel.serviceDeliveryType
            )
        )
        ScreenManager.showScreen(serviceOrderFragment)
    }

}