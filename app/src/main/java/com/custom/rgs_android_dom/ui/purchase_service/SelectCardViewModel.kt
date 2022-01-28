package com.custom.rgs_android_dom.ui.purchase_service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase_service.model.CardModel
import com.custom.rgs_android_dom.domain.purchase_service.model.PurchaseInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectCardViewModel(
    purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val savedCardsController = MutableLiveData<List<CardModel>>()
    val savedCardsObserver: LiveData<List<CardModel>> = savedCardsController

    init {
        purchaseInteractor.getSavedCards()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    savedCardsController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

}