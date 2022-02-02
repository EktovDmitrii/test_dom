package com.custom.rgs_android_dom.ui.purchase

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase_service.model.*
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ADD_PROPERTY
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.edit_purchase_date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressFragment
import com.custom.rgs_android_dom.utils.DATE_PATTERN_DATE_AND_TIME_FOR_PURCHASE
import com.custom.rgs_android_dom.utils.formatTo
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class PurchaseViewModel(
    private val model: PurchaseModel,
    private val propertyInteractor: PropertyInteractor,
    private val clientInteractor: ClientInteractor,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private var propertyListSize: Int? = null

    private val purchaseController = MutableLiveData(model)
    val purchaseObserver: LiveData<PurchaseModel> = purchaseController

    private val isEnableButtonController = MutableLiveData(false)
    val isEnableButtonObserver: LiveData<Boolean> = isEnableButtonController

    private val hasCodeAgentController = MutableLiveData<Boolean>()
    val hasCodeAgentObserver: LiveData<Boolean> = hasCodeAgentController

    init {
        clientInteractor.getClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    hasCodeAgentController.value = it.hasAgentInfo
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        propertyInteractor.getAllProperty()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    propertyListSize = it.size
                    if (it.isNotEmpty()){
                        updateAddress(it.last())
                    }
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    private fun validateFields() {
        isEnableButtonController.value = purchaseObserver.value?.email != null &&
                purchaseObserver.value?.card != null &&
                purchaseObserver.value?.purchaseDateTimeModel != null &&
                purchaseObserver.value?.propertyItemModel != null
    }

    fun updateAddress(propertyItemModel: PropertyItemModel) {
        val newValue = purchaseController.value
        newValue?.propertyItemModel = propertyItemModel
        newValue?.let {
            purchaseController.value = it
            validateFields()
        }
    }

    fun updateEmail(email: String) {
        purchaseController.value?.let {
            purchaseController.value = it.copy(email = email)
            validateFields()
        }
    }

    fun updateAgentCode(code: String) {
        purchaseController.value?.let {
            purchaseController.value = it.copy(agentCode = code)
            validateFields()
        }
    }

    fun updateCard(card: CardModel) {
        purchaseController.value?.let {
            purchaseController.value = it.copy(card = card)
            validateFields()
        }
    }

    fun updateComment(comment: String) {
        val newValue = purchaseController.value
        newValue?.comment = comment
        newValue?.let { purchaseController.postValue(it) }
    }

    fun updateDateTime(purchaseDateTimeModel: PurchaseDateTimeModel) {
        purchaseController.value?.let {
            purchaseController.value = it.copy(purchaseDateTimeModel = purchaseDateTimeModel)
            validateFields()
        }
    }

    fun onAddressClick(childFragmentManager: FragmentManager) {
        val purchaseAddressFragment = SelectPurchaseAddressFragment.newInstance(purchaseController.value?.propertyItemModel)
        purchaseAddressFragment.show(childFragmentManager,purchaseAddressFragment.TAG)
    }

    fun onDateTimeClick(childFragmentManager: FragmentManager) {
        val currentPurchaseDateTimeModel = purchaseController.value?.purchaseDateTimeModel

        if (currentPurchaseDateTimeModel != null) {
            val purchaseDateTimeFragment = PurchaseDateTimeFragment.newInstance(
                currentPurchaseDateTimeModel
            )
            purchaseDateTimeFragment.show(childFragmentManager, purchaseDateTimeFragment.TAG)
        } else {
            val purchaseDateTimeFragment = PurchaseDateTimeFragment.newInstance(
                PurchaseDateTimeModel()
            )
            purchaseDateTimeFragment.show(childFragmentManager, purchaseDateTimeFragment.TAG)
        }

    }

    fun onCardClick(childFragmentManager: FragmentManager) {
        val cardFragment = SelectCardBottomFragment.newInstance()
        cardFragment.show(childFragmentManager, cardFragment.TAG)
    }

    fun onEmailClick(childFragmentManager: FragmentManager) {
        val emailBottomFragment = AddEmailBottomFragment()
        emailBottomFragment.show(childFragmentManager, emailBottomFragment.TAG)
    }

    fun onCodeAgentClick(childFragmentManager: FragmentManager) {
        val codeAgentBottomFragment = AddAgentBottomFragment()
        codeAgentBottomFragment.show(childFragmentManager, codeAgentBottomFragment.TAG)
    }

    fun makeOrder(navigateId: Int) {
        purchaseObserver.value?.let { purchase ->
            purchaseInteractor.makeProductPurchase(
                productId = purchase.id,
                bindingId = if (purchase.card is SavedCardModel) {
                    purchase.card.id
                } else {
                    null
                },
                email = purchase.email!!,
                objectId = purchase.propertyItemModel!!.id,
                saveCard = if (purchase.card is NewCardModel) {
                    purchase.card.doSave
                } else {
                    true
                },
                deliveryDate = purchase.purchaseDateTimeModel?.date?.formatTo(
                    DATE_PATTERN_DATE_AND_TIME_FOR_PURCHASE
                ) + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).removePrefix("GMT"),
                timeFrom = purchase.purchaseDateTimeModel?.selectedPeriodModel!!.timeInterval.split(
                    "–"
                )[0],
                timeTo = purchase.purchaseDateTimeModel?.selectedPeriodModel!!.timeInterval.split("–")[1]
            )
                .doOnSubscribe { isEnableButtonController.postValue(false) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isEnableButtonController.value = true }
                .subscribeBy(
                    onSuccess = {
                        ScreenManager.showScreenScope(
                            PaymentWebViewFragment.newInstance(
                                url = it,
                                productId = purchase.id,
                                email = purchase.email,
                                price = purchase.price?.amount.toString(),
                                fragmentId = navigateId
                            ), PAYMENT
                        )
                    },
                    onError = {
                        logException(this, it)
                        handleNetworkException(it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }
}
