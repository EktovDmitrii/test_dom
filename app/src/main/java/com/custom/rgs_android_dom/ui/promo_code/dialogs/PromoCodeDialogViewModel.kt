package com.custom.rgs_android_dom.ui.promo_code.dialogs

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenInfo
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.modal.ModalPromoCodesFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PromoCodeDialogViewModel(
    private val promoCode: String,
    private val purchaseModel: PurchaseModel?,
    private var shouldShowAgentView: Boolean,
    private val promoCodesInteractor: PromoCodesInteractor,
    private val chatInteractor: ChatInteractor
) : BaseViewModel() {

    private val promoCodesController = MutableLiveData<PromoCodeItemModel>()
    val promoCodesObserver: LiveData<PromoCodeItemModel> = promoCodesController

    private val getPromoCodesController = MutableLiveData<Boolean>()
    val getPromoCodesObserver: LiveData<Boolean> = getPromoCodesController

    init {
        promoCodesInteractor.activatePromoCode(promoCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    promoCodesController.value = it
                    loadingStateController.value = LoadingState.CONTENT
                },
                onError = {
                    loadingStateController.value = LoadingState.ERROR
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onPromoCodeClick(parentFragmentManager: FragmentManager) {
        if (purchaseModel == null) {
            getPromoCodesController.value = true
        } else {
            getPromoCodesController.value = false
            val modalPromoCodes = ModalPromoCodesFragment.newInstance(purchaseModel, null)
            modalPromoCodes.show(parentFragmentManager, modalPromoCodes.TAG)
        }
        close()
    }

    fun onChangeDataFailureClick(parentFragmentManager: FragmentManager) {
        val addPromoCodeFragment = AddPromoCodeFragment.newInstance(shouldShowAgentView, purchaseModel)
        addPromoCodeFragment.show(parentFragmentManager, addPromoCodeFragment.TAG)
        close()
    }

    fun onChatFailureClick() {
        val screenInfo = ScreenInfo(targetScreen = TargetScreen.PROMO_CODE, null)
        ScreenManager.showBottomScreen(
            ChatFragment.newInstance(
                chatInteractor.getMasterOnlineCase(),
                screenInfo
            )
        )
        close()
    }
}
