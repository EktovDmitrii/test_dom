package com.custom.rgs_android_dom.ui.promo_code.modal

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.ui.purchase.PurchaseFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ModalPromoCodesViewModel(
    private val promoCodesInteractor: PromoCodesInteractor,
    private val clientInteractor: ClientInteractor,
    private val purchaseModel: PurchaseModel
) : BaseViewModel() {

    private val promoCodesController = MutableLiveData<List<PromoCodeItemModel>>()
    val promoCodesObserver: LiveData<List<PromoCodeItemModel>> = promoCodesController

    private val applyPromoCodeController = MutableLiveData<PromoCodeItemModel>()
    private val agentCodeController = MutableLiveData<Boolean>()

    init {
        getAgent()

        promoCodesInteractor.getOrderPromoCodes(purchaseModel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { promoCodesController.value = it },
                onError = {
                    promoCodesController.value = emptyList()
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onAddClick(parentFragmentManager: FragmentManager) {
        agentCodeController.value?.let {
            val emailBottomFragment = AddPromoCodeFragment.newInstance(it, purchaseModel)
            emailBottomFragment.show(parentFragmentManager, emailBottomFragment.TAG)
            close()
        }
    }

    fun onApplyClick() {
        applyPromoCodeController.value?.let {
            val purchaseFragment = PurchaseFragment.newInstance(purchaseModel, it)
            ScreenManager.showBottomScreen(purchaseFragment)
        }
        close()
    }

    fun saveApplyPromoCode(promoCodeModel: PromoCodeItemModel) {
        applyPromoCodeController.value = promoCodeModel
    }

    private fun getAgent() {
        clientInteractor.getAgent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    agentCodeController.value = it.agentCode.isNotEmpty()
                },
                onError = {
                    agentCodeController.value = false
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }
}
