package com.custom.rgs_android_dom.ui.promo_code

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodeItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.info_promo_code.InfoPromoCodeFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PromoCodesViewModel(
    private val promoCodesInteractor: PromoCodesInteractor,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val promoCodesController = MutableLiveData<List<PromoCodeItemModel>>()
    val promoCodesObserver: LiveData<List<PromoCodeItemModel>> = promoCodesController

    private val isAgentCodeVisibleController = MutableLiveData<Boolean>()

    init {
        getAgent()
        getPromoCodes()
    }

    fun getPromoCodes() {
        promoCodesInteractor.getPromoCodes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    promoCodesController.value = it
                },
                onError = {
                    loadingStateController.value = LoadingState.ERROR
                    promoCodesController.value = emptyList()
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        close()
        ScreenManager.showBottomScreen(ClientFragment())
    }

    fun onItemClick(promoCode: PromoCodeItemModel, childFragmentManager: FragmentManager) {
        val infoPromoCodeFragment = InfoPromoCodeFragment.newInstance(promoCode)
        infoPromoCodeFragment.show(childFragmentManager, infoPromoCodeFragment.TAG)
    }

    fun onAddClick(childFragmentManager: FragmentManager) {
        isAgentCodeVisibleController.value?.let {
            val addPromoCodeFragment = AddPromoCodeFragment.newInstance(it, null)
            addPromoCodeFragment.show(childFragmentManager, addPromoCodeFragment.TAG)
        }
    }

    private fun getAgent() {
        clientInteractor.getAgent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    isAgentCodeVisibleController.value = it.agentCode.isNotEmpty()
                },
                onError = {
                    isAgentCodeVisibleController.value = false
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }
}
