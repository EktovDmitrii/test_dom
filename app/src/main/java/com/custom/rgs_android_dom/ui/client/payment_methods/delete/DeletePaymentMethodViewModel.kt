package com.custom.rgs_android_dom.ui.client.payment_methods.delete

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toMSDErrorModel
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DeletePaymentMethodViewModel(
    private val bindingId: String,
    private val purchaseInteractor: PurchaseInteractor
) : BaseViewModel() {

    private val showErrorDialogController = MutableLiveData<String>()
    val showErrorDialogObserver: LiveData<String> = showErrorDialogController

    fun onDeleteClick() {
        purchaseInteractor.deleteCard(bindingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    close()
                },
                onError = {
                    logException(this, it)
                    it.toMSDErrorModel()?.let {
                        showErrorDialogController.value = it.code
                    }
                }
            ).addTo(dataCompositeDisposable)
    }


}