package com.custom.rgs_android_dom.ui.catalog.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.purchase.model.PurchaseModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceFragment
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceLauncher
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatTo

class ProductViewModel(
    private val product: ProductLauncher,
    private val registrationInteractor: RegistrationInteractor,
    private val catalogInteractor: CatalogInteractor,
    private val propertyInteractor: PropertyInteractor,
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    private val productServicesController = MutableLiveData<List<ServiceShortModel>>()
    val productServicesObserver: LiveData<List<ServiceShortModel>> = productServicesController

    private val productAddressController = MutableLiveData<String?>()
    val productAddressObserver: LiveData<String?> = productAddressController

    private val productValidToController = MutableLiveData<String?>()
    val productValidToObserver: LiveData<String?> = productValidToController

    private val productPaidDateController = MutableLiveData<String?>()
    val productPaidDateObserver: LiveData<String?> = productPaidDateController

    init {
        productValidToController.value = product.purchaseValidTo?.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        productPaidDateController.value = product.paidDate?.formatTo(DATE_PATTERN_DATE_ONLY)

        catalogInteractor.getProduct(product.productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { product ->
                    productController.value = product.copy(isPurchased = this.product.isPurchased)
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        catalogInteractor.getProductServices(product.productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    productServicesController.value = it.map { it.copy(isPurchased = product.isPurchased) }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        product.purchaseObjectId?.let { objectId ->
            propertyInteractor.getPropertyItem(objectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        productAddressController.value = it.address?.address
                    },
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onCheckoutClick() {
        if (registrationInteractor.isAuthorized()) {
            productController.value?.let {
                val purchaseServiceModel = PurchaseModel(
                    id = it.id,
                    defaultProduct = it.defaultProduct,
                    duration = it.duration,
                    iconLink = it.iconLink,
                    name = it.name,
                    price = it.price
                )
                val purchaseFragment = PurchaseFragment.newInstance(purchaseServiceModel)
                ScreenManager.showScreenScope(purchaseFragment, PAYMENT)
            }
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onServiceClick(serviceShortModel: ServiceShortModel) {
        serviceShortModel.serviceId?.let {
            val serviceFragment = ServiceFragment.newInstance(
                ServiceLauncher(
                    productId = product.productId,
                    serviceId = serviceShortModel.serviceId,
                    isPurchased = product.isPurchased
                )
            )
            ScreenManager.showBottomScreen(serviceFragment)
        }
    }
}
