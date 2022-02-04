package com.custom.rgs_android_dom.ui.catalog.product.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ProductModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_FULL_MONTH
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_ONLY
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SingleProductViewModel(
    private val product: SingleProductLauncher,
    private val catalogInteractor: CatalogInteractor,
    private val propertyInteractor: PropertyInteractor
    ) : BaseViewModel() {

    private val productController = MutableLiveData<ProductModel>()
    val productObserver: LiveData<ProductModel> = productController

    private val productAddressController = MutableLiveData<String?>()
    val productAddressObserver: LiveData<String?> = productAddressController

    private val productValidToController = MutableLiveData<String?>()
    val productValidToObserver: LiveData<String?> = productValidToController

    private val productPaidDateController = MutableLiveData<String?>()
    val productPaidDateObserver: LiveData<String?> = productPaidDateController

    init {
        productValidToController.value = product.purchaseValidTo?.formatTo(
            DATE_PATTERN_DATE_FULL_MONTH
        )
        productPaidDateController.value = product.paidDate?.formatTo(DATE_PATTERN_DATE_ONLY)

        catalogInteractor.getProduct(product.productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { product->
                    productController.value = product.copy(
                        isIncluded = this.product.isIncluded,
                        isPurchased = this.product.isPurchased
                    )
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

    fun onBackClick(){
        closeController.value = Unit
    }

}