package com.custom.rgs_android_dom.ui.purchase.select.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.purchase.model.CardModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SelectCardViewModel(
    private var selectedCard: CardModel?,
    purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val savedCardsController = MutableLiveData<List<CardModel>>()
    val savedCardsObserver: LiveData<List<CardModel>> = savedCardsController

    init {
        purchaseInteractor.getSavedCards()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {savedCards->
                savedCards.find { it.id == selectedCard?.id }?.isSelected = true
                return@map savedCards
            }
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