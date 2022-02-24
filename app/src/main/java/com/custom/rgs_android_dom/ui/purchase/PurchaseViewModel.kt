package com.custom.rgs_android_dom.ui.purchase

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.*
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.add.agent.AddAgentFragment
import com.custom.rgs_android_dom.ui.purchase.add.comment.AddCommentFragment
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressFragment
import com.custom.rgs_android_dom.ui.purchase.select.card.SelectCardFragment
import com.custom.rgs_android_dom.ui.purchase.add.email.AddEmailFragment
import com.custom.rgs_android_dom.ui.purchase.payments.PaymentWebViewFragment
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

        clientInteractor.getPersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {personalData->
                    purchaseController.value?.let {
                        purchaseController.value = it.copy(email = personalData.email.ifEmpty { null })
                        validateFields()
                    }
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
        if (purchaseObserver.value?.defaultProduct == true){
            isEnableButtonController.value = purchaseObserver.value?.email != null &&
                    purchaseObserver.value?.card != null &&
                    (purchaseObserver.value?.deliveryType == DeliveryType.ONLINE || purchaseObserver.value?.propertyItemModel != null) &&
                    purchaseObserver.value?.purchaseDateTimeModel != null
        } else {
            isEnableButtonController.value = purchaseObserver.value?.email != null &&
                    purchaseObserver.value?.card != null &&
                    purchaseObserver.value?.propertyItemModel != null
        }

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
        val purchaseDateTimeFragment = PurchaseDateTimeFragment.newInstance()
        purchaseDateTimeFragment.show(childFragmentManager, purchaseDateTimeFragment.TAG)
    }

    fun onCardClick(childFragmentManager: FragmentManager) {
        val cardFragment = SelectCardFragment.newInstance(purchaseController.value?.card)
        cardFragment.show(childFragmentManager, cardFragment.TAG)
    }

    fun onEmailClick(childFragmentManager: FragmentManager) {
        val emailBottomFragment = AddEmailFragment.newInstance(purchaseController.value?.email)
        emailBottomFragment.show(childFragmentManager, emailBottomFragment.TAG)
    }

    fun onCodeAgentClick(childFragmentManager: FragmentManager) {
        if (purchaseController.value?.agentCode.isNullOrEmpty()){
            val addAgentFragment = AddAgentFragment()
            addAgentFragment.show(childFragmentManager, addAgentFragment.TAG)
        }
    }

    fun onAddCommentClick(childFragmentManager: FragmentManager){
        val editPurchaseServiceComment = AddCommentFragment.newInstance(purchaseController.value?.comment)
        editPurchaseServiceComment.show(childFragmentManager, editPurchaseServiceComment.TAG)
    }

    fun updateAgentCode(code: String) {
        purchaseController.value?.let {
            purchaseController.value = it.copy(agentCode = code)
            validateFields()
        }
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
                comment = purchase.comment,
                saveCard = if (purchase.card is NewCardModel) {
                    purchase.card.doSave
                } else {
                    true
                },
                deliveryDate = purchase.purchaseDateTimeModel?.selectedDate?.formatTo(
                    DATE_PATTERN_DATE_AND_TIME_FOR_PURCHASE
                ) + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).removePrefix("GMT"),
                timeFrom = purchase.purchaseDateTimeModel?.selectedPeriodModel?.timeFrom,
                timeTo = purchase.purchaseDateTimeModel?.selectedPeriodModel?.timeTo,
                withOrder = purchase.defaultProduct && purchase.price?.fix == false
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
