package com.custom.rgs_android_dom.ui.purchase.payments.success

import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.toSpannable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PaymentSuccessViewModel(
    private val productId: String,
    email: String,
    private val purchaseInteractor: PurchaseInteractor,
    private val catalogInteractor: CatalogInteractor
) : BaseViewModel() {

    private val emailController = MutableLiveData<Spannable>()
    val emailObserver: LiveData<Spannable> = emailController

    init {
        emailController.value = SpannableStringBuilder()
            .append("Чек об оплате отправлен на вашу почту ")
            .bold { append(email) }
            .toSpannable()

        purchaseInteractor.notifyProductPurchased(productId)
    }

    fun onCloseScope() {
        closeController.value = Unit
    }

    fun onMoreClick(){
        closeController.value = Unit
        catalogInteractor.getProduct(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {product->
                    if (product.defaultProduct){

                    } else {
                        ScreenManager.showBottomScreen(
                            ProductFragment.newInstance(
                                ProductLauncher(
                                    productId = productId,
                                    isPurchased = true,
                                    purchaseValidTo = product.validityTo
                                )
                            )
                        )
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}