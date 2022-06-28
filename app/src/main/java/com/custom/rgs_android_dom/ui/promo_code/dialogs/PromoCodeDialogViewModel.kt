package com.custom.rgs_android_dom.ui.promo_code.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenInfo
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.navigation.TargetScreen
import com.custom.rgs_android_dom.ui.promo_code.PromoCodesFragment
import com.custom.rgs_android_dom.utils.ProgressTransformer
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PromoCodeDialogViewModel(
    private val promoCodeId: String,
    private val promoCodesInteractor: PromoCodesInteractor,
    private val chatInteractor: ChatInteractor,
) : BaseViewModel() {

    private val promoCodesController = MutableLiveData<PromoCodeItemModel>()
    val promoCodesObserver: LiveData<PromoCodeItemModel> = promoCodesController

    init {
        promoCodesInteractor.activatePromoCode(promoCodeId)
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

    fun onPromoCodeClick() {
        ScreenManager.showScreen(PromoCodesFragment())
        close()
    }

    fun onChatFailureClick() {
        val screenInfo = ScreenInfo(targetScreen = TargetScreen.PROMO_CODE, null)
        ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase(), screenInfo))
        close()
    }
}
