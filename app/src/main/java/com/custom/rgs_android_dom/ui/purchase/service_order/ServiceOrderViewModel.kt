package com.custom.rgs_android_dom.ui.purchase.service_order

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.catalog.CatalogInteractor
import com.custom.rgs_android_dom.domain.catalog.models.ServiceModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.*
import com.custom.rgs_android_dom.domain.purchase.view_states.ServiceOrderViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.order_detail.OrderDetailFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.add.comment.AddCommentFragment
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressFragment
import com.custom.rgs_android_dom.utils.ProgressTransformer
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ServiceOrderViewModel(
    private val serviceId: String,
    private val productId: String,
    private val propertyInteractor: PropertyInteractor,
    private val catalogInteractor: CatalogInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private var propertyCount: Int? = null

    private val serviceController = MutableLiveData<ServiceModel>()
    val serviceObserver: LiveData<ServiceModel> = serviceController

    private val serviceOrderViewStateController = MutableLiveData<ServiceOrderViewState>()
    val serviceOrderViewStateObserver: LiveData<ServiceOrderViewState> = serviceOrderViewStateController

    private val isOrderPerformingController = MutableLiveData<Boolean>()
    val isOrderPerformingObserver: LiveData<Boolean> = isOrderPerformingController

    init {
        Single.zip(
            catalogInteractor.getProductServiceDetails(productId, serviceId),
            propertyInteractor.getAllProperty()
        ){ service, propertyList ->
            propertyCount = propertyList.size
            serviceController.postValue(service)

            purchaseInteractor.setPropertyOptional(service.deliveryType)
        }
        .compose(
            ProgressTransformer(
                onLoading = {
                    loadingStateController.postValue(LoadingState.LOADING)
                },
                onError = {
                    logException(this, it)
                    loadingStateController.value = LoadingState.ERROR
                },
                onLoaded = {
                    loadingStateController.value = LoadingState.CONTENT
                }
            )
        )
        .subscribe()
        .addTo(dataCompositeDisposable)

        purchaseInteractor.serviceOrderViewStateSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    serviceOrderViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun onSelectPropertyClick(childFragmentManager: FragmentManager) {
        val purchaseAddressFragment = SelectPurchaseAddressFragment.newInstance(serviceOrderViewStateController.value?.property)
        purchaseAddressFragment.show(childFragmentManager,purchaseAddressFragment.TAG)
    }

    fun onSelectOrderDateClick(childFragmentManager: FragmentManager) {
        val purchaseDateTimeFragment = PurchaseDateTimeFragment.newInstance()
        purchaseDateTimeFragment.show(childFragmentManager, purchaseDateTimeFragment.TAG)
    }

    fun onAddCommentClick(childFragmentManager: FragmentManager){
        val editPurchaseServiceComment = AddCommentFragment.newInstance(serviceOrderViewStateController.value?.comment)
        editPurchaseServiceComment.show(childFragmentManager, editPurchaseServiceComment.TAG)
    }

    fun onPropertySelected(property: PropertyItemModel){
        purchaseInteractor.selectServiceOrderProperty(property)
    }

    fun onCommentSelected(comment: String){
        purchaseInteractor.selectServiceOrderComment(comment)
    }

    fun onOrderDateSelected(orderDate: PurchaseDateTimeModel){
        purchaseInteractor.selectServiceOrderDate(orderDate)
    }

    fun onOrderClick(){
        purchaseInteractor.orderServiceOnBalance(productId, serviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isOrderPerformingController.value = true
            }
            .subscribeBy(
                onSuccess = {order->
                    isOrderPerformingController.value = false
                    val orderFragment = OrderDetailFragment.newInstance(order)
                    ScreenManager.showScreen(orderFragment)
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                    isOrderPerformingController.value = false
                }
            ).addTo(dataCompositeDisposable)
    }

}
