package com.custom.rgs_android_dom.ui.promo_code

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.promo_codes.PromoCodesInteractor
import com.custom.rgs_android_dom.domain.promo_codes.model.PromoCodesItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.utils.ProgressTransformer
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PromoCodesViewModel(
    private val promoCodesInteractor: PromoCodesInteractor,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val promoCodesController = MutableLiveData<List<PromoCodesItemModel>>()
    val promoCodesObserver: LiveData<List<PromoCodesItemModel>> = promoCodesController

    private val agentCodeController = MutableLiveData<Boolean>()

    init {
        getAgent()

        promoCodesInteractor.getPromoCodes()
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
                onSuccess = { promoCodesController.value = it },
                onError = {
                    promoCodesController.value = emptyList()
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        close()
    }

    fun onAddClick(childFragmentManager: FragmentManager) {
        agentCodeController.value?.let {
            val emailBottomFragment = AddPromoCodeFragment.newInstance(it)
            emailBottomFragment.show(childFragmentManager, emailBottomFragment.TAG)
        }
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
