package com.custom.rgs_android_dom.ui.purchase

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.property.PropertyInteractor
import com.custom.rgs_android_dom.domain.property.models.PropertyItemModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.*
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.promo_code.modal.ModalPromoCodesFragment
import com.custom.rgs_android_dom.ui.purchase.add.agent.AddAgentFragment
import com.custom.rgs_android_dom.ui.purchase.add.comment.AddCommentFragment
import com.custom.rgs_android_dom.ui.purchase.add.email.AddEmailFragment
import com.custom.rgs_android_dom.ui.purchase.payments.PaymentWebViewFragment
import com.custom.rgs_android_dom.ui.purchase.payments.error.PaymentErrorFragment
import com.custom.rgs_android_dom.ui.purchase.select.address.SelectPurchaseAddressFragment
import com.custom.rgs_android_dom.ui.purchase.select.card.SelectCardFragment
import com.custom.rgs_android_dom.ui.purchase.select.date_time.PurchaseDateTimeFragment
import com.custom.rgs_android_dom.utils.logException
import com.yandex.metrica.YandexMetrica
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTimeZone

class PurchaseViewModel(
    private val model: PurchaseModel,
    private val propertyInteractor: PropertyInteractor,
    private val clientInteractor: ClientInteractor,
    private val purchaseInteractor: PurchaseInteractor,
    private val promoCodesInteractor: PromoCodesInteractor,
) : BaseViewModel() {

    private var propertyListSize: Int? = null

    private val purchaseController = MutableLiveData(model)
    val purchaseObserver: LiveData<PurchaseModel> = purchaseController

    private val isEnableButtonController = MutableLiveData(false)
    val isEnableButtonObserver: LiveData<Boolean> = isEnableButtonController

    private val hasCodeAgentController = MutableLiveData<Boolean>()
    val hasCodeAgentObserver: LiveData<Boolean> = hasCodeAgentController

    private val hasPromoCodeController = MutableLiveData<PromoCodeItemModel?>()
    val hasPromoCodeObserver: LiveData<PromoCodeItemModel?> = hasPromoCodeController

    private val showDiscountLayoutController = MutableLiveData<Boolean>()
    val showDiscountLayoutObserver: LiveData<Boolean> = showDiscountLayoutController

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
                onSuccess = { personalData ->
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

        purchaseInteractor.getProductPurchasedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    close()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        closeController.value = Unit
    }

    fun checkPromoCode() {
        purchaseController.value?.let { purchaseModel ->
            promoCodesInteractor.getOrderPromoCodes(purchaseModel.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
                .subscribeBy(
                    onSuccess = { listPromoCodes ->
                        hasPromoCodeController.value?.let { promoCode ->
                            listPromoCodes.filter {
                                it.id == promoCode.id
                            }
                        }
                        if (listPromoCodes.isNotEmpty()) {
                            loadingStateController.value = LoadingState.CONTENT
                        } else {
                            loadingStateController.value = LoadingState.ERROR
                        }
                    },
                    onError = {
                        loadingStateController.value = LoadingState.ERROR
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }

    private fun validateFields() {
        if (purchaseObserver.value?.defaultProduct == true) {
            isEnableButtonController.value = purchaseObserver.value?.email != null &&
                    purchaseObserver.value?.card != null &&
                    (purchaseObserver.value?.deliveryType == DeliveryType.ONLINE || purchaseObserver.value?.propertyItemModel != null) &&
                    purchaseObserver.value?.purchaseDateTimeModel != null
        } else {
            isEnableButtonController.value = purchaseObserver.value?.email != null &&
                    purchaseObserver.value?.card != null
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
        val purchaseAddressFragment =
            SelectPurchaseAddressFragment.newInstance(purchaseController.value?.propertyItemModel)
        purchaseAddressFragment.show(childFragmentManager, purchaseAddressFragment.TAG)
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
        if (purchaseController.value?.agentCode.isNullOrEmpty()) {
            val addAgentFragment = AddAgentFragment()
            addAgentFragment.show(childFragmentManager, addAgentFragment.TAG)
        }
    }

    fun onAddCommentClick(childFragmentManager: FragmentManager) {
        val editPurchaseServiceComment = AddCommentFragment.newInstance(purchaseController.value?.comment)
        editPurchaseServiceComment.show(childFragmentManager, editPurchaseServiceComment.TAG)
    }

    fun onAddPromoCodeClick(childFragmentManager: FragmentManager) {
        purchaseController.value?.let {
            val modalPromoCodes = ModalPromoCodesFragment.newInstance(it, hasPromoCodeController.value)
            modalPromoCodes.show(childFragmentManager, modalPromoCodes.TAG)
        }
    }

    fun onDeletePromoCodeClick() {
        hasPromoCodeController.value = null
        showDiscountLayoutController.value = false
    }

    fun updateAgentCode(code: String) {
        purchaseController.value?.let {
            purchaseController.value = it.copy(agentCode = code)
            validateFields()
        }
    }

    fun updatePromoCode(promoCode: PromoCodeItemModel) {
        hasPromoCodeController.value = promoCode
        showDiscountLayoutController.value = true
    }

    fun makeOrder() {
        purchaseObserver.value?.let { purchase ->
            purchaseInteractor.makeProductPurchase(
                productId = purchase.id,
                bindingId = if (purchase.card is SavedCardModel) {
                    purchase.card.id
                } else {
                    null
                },
                email = purchase.email!!,
                objectId = purchase.propertyItemModel?.id ?: "",
                comment = purchase.comment,
                saveCard = if (purchase.card is NewCardModel) {
                    purchase.card.doSave
                } else {
                    true
                },
                deliveryDate = purchase.purchaseDateTimeModel?.selectedDate?.toDateTime(
                    DateTimeZone.UTC).toString(),
                timeFrom = purchase.purchaseDateTimeModel?.selectedPeriodModel?.timeFrom,
                timeTo = if (purchase.purchaseDateTimeModel?.selectedPeriodModel?.timeFrom == "18:00")
                    purchase.purchaseDateTimeModel.selectedPeriodModel?.copy(timeTo = "23:59")?.timeTo
                else purchase.purchaseDateTimeModel?.selectedPeriodModel?.timeTo,
                withOrder = purchase.defaultProduct,
                clientPromoCodeId = hasPromoCodeController.value?.id
            )
                .doOnSubscribe {
                    isEnableButtonController.postValue(false)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { isEnableButtonController.value = true }
                .subscribeBy(
                    onSuccess = {
                        YandexMetrica.reportEvent("product_order_finish", "{\"order_id\":\"${it.orderId}\",\"product_item\":\"${purchase.name}\"}")

                        ScreenManager.showBottomScreen(
                            PaymentWebViewFragment.newInstance(
                                url = it.paymentUrl,
                                productId = purchase.id,
                                productVersionId = purchase.versionId,
                                email = purchase.email,
                                orderId = it.orderId,
                            )
                        )
                    },
                    onError = {
                        logException(this, it)

                        ScreenManager.showScreenScope(
                            PaymentErrorFragment(),
                            PAYMENT
                        )
                    }
                ).addTo(dataCompositeDisposable)
        }
    }
}
