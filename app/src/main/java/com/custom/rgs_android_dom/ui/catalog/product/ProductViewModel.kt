package com.custom.rgs_android_dom.ui.catalog.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.*
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceFragment
import com.custom.rgs_android_dom.ui.catalog.product.service.ServiceLauncher
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderFragment
import com.custom.rgs_android_dom.ui.purchase.service_order.ServiceOrderLauncher
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
import com.custom.rgs_android_dom.utils.formatTo
import com.yandex.metrica.YandexMetrica

class ProductViewModel(
    private val product: ProductLauncher,
    private val registrationInteractor: RegistrationInteractor,
    private val catalogInteractor: CatalogInteractor,
    private val propertyInteractor: PropertyInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    private val productServicesController = MutableLiveData<List<ServiceShortModel>>()
    val productServicesObserver: LiveData<List<ServiceShortModel>> = productServicesController

    private val productAddressController = MutableLiveData<String?>()
    val productAddressObserver: LiveData<String?> = productAddressController

    private val isGoneButtonController = MutableLiveData<Boolean>()
    val isGoneButtonObserver: LiveData<Boolean> = isGoneButtonController

    private val productValidityFromToController = MutableLiveData<Pair<String, String>?>()
    val productValidityFromToObserver: LiveData<Pair<String, String>?> = productValidityFromToController

    private val isGoneOrderTextView = product.purchaseValidFrom != null && product.purchaseValidFrom.isAfterNow

    init {
        setValidityDate()
        isGoneButtonController.value = isGoneOrderTextView || product.isPurchased

        catalogInteractor.getProduct(product.productId, product.productVersionId)
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

        purchaseInteractor.getServiceOrderedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    getIncludedServices()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        getIncludedServices()
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    private fun setValidityDate() {
        productValidityFromToController.value = if (product.purchaseValidFrom != null && product.purchaseValidFrom.isAfterNow){
            "Действует с" to product.purchaseValidFrom.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        } else if (product.purchaseValidTo != null && product.purchaseValidTo.isAfterNow){
            "Действует до" to product.purchaseValidTo.formatTo(DATE_PATTERN_DATE_FULL_MONTH)
        } else {
            null
        }
    }

    fun onCheckoutClick() {
        if (registrationInteractor.isAuthorized()) {
            productController.value?.let {
                YandexMetrica.reportEvent("product_order_start", "{\"product_item\":\"${it.name}\"}")

                val purchaseServiceModel = PurchaseModel(
                    id = it.id,
                    versionId = it.versionId ?: "",
                    defaultProduct = it.defaultProduct,
                    duration = it.duration,
                    deliveryTime = it.deliveryTime,
                    logoSmall = it.logoSmall,
                    name = it.name,
                    price = it.price
                )
                val purchaseFragment = PurchaseFragment.newInstance(purchaseServiceModel)
                //ScreenManager.showScreenScope(purchaseFragment, PAYMENT)
                ScreenManager.showBottomScreen(purchaseFragment)
            }
        } else {
            ScreenManager.showScreenScope(RegistrationPhoneFragment(), REGISTRATION)
        }
    }

    fun onServiceClick(serviceShortModel: ServiceShortModel) {
        val serviceFragment = ServiceFragment.newInstance(
            ServiceLauncher(
                productId = product.productId,
                serviceId = serviceShortModel.serviceId,
                serviceVersionId = serviceShortModel.serviceVersionId,
                isPurchased = product.isPurchased,
                purchaseValidFrom = product.purchaseValidFrom,
                purchaseValidTo = product.purchaseValidTo,
                purchaseObjectId = product.purchaseObjectId,
                quantity = serviceShortModel.quantity,
                canBeOrdered = serviceShortModel.canBeOrdered
            )
        )
        ScreenManager.showBottomScreen(serviceFragment)
    }

    fun onServiceOrderClick(serviceShortModel: ServiceShortModel){
        val serviceOrderFragment = ServiceOrderFragment.newInstance(
            ServiceOrderLauncher(serviceShortModel.serviceId, product.productId, serviceShortModel.serviceVersionId)
        )
        ScreenManager.showScreen(serviceOrderFragment)
    }

    private fun getIncludedServices(){
        catalogInteractor.getProductServices(product.productId, product.productVersionId, product.isPurchased, product.purchaseValidFrom)
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
    }
}
