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
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesFragment
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.modal.ModalPromoCodesFragment
import com.custom.rgs_android_dom.utils.ProgressTransformer
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

    var showOrderTextButton = false

    private val promoCodesController = MutableLiveData<PromoCodeItemModel>()
    val promoCodesObserver: LiveData<PromoCodeItemModel> = promoCodesController

    init {
        if (purchaseModel == null) showOrderTextButton = true

        promoCodesInteractor.activatePromoCode(promoCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
            .subscribeBy(
                onSuccess = {
                    promoCodesController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onPromoCodeClick(parentFragmentManager: FragmentManager) {
        if (purchaseModel == null) {
            ScreenManager.showScreen(PromoCodesFragment())
        } else {
            val modalPromoCodes = ModalPromoCodesFragment.newInstance(purchaseModel)
            modalPromoCodes.show(parentFragmentManager, modalPromoCodes.TAG)
        }
        close()
    }

    fun onChangeDataFailureClick(parentFragmentManager: FragmentManager) {
        val emailBottomFragment = AddPromoCodeFragment.newInstance(shouldShowAgentView, purchaseModel)
        emailBottomFragment.show(parentFragmentManager, emailBottomFragment.TAG)
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
