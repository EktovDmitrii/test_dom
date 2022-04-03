package com.custom.rgs_android_dom.ui.client.payment_methods

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.purchase.PurchaseInteractor
import com.custom.rgs_android_dom.domain.purchase.models.NewCardModel
import com.custom.rgs_android_dom.domain.purchase.models.SavedCardModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PaymentMethodsViewModel(
    private val purchaseInteractor: PurchaseInteractor,
    private val chatInteractor: ChatInteractor
) : BaseViewModel() {

    private val paymentMethodsController = MutableLiveData<List<SavedCardModel>?>()
    val paymentMethodsObserver: LiveData<List<SavedCardModel>?> = paymentMethodsController

    private val isInEditModeController = MutableLiveData<Boolean>()
    val isInEditModeObserver: LiveData<Boolean> = isInEditModeController

    private val noPaymentMethodsController = MutableLiveData<Boolean>()
    val noPaymentMethodsObserver: LiveData<Boolean> = noPaymentMethodsController

    private val showDeleteFragmentController = MutableLiveData<String>()
    val showDeleteFragmentObserver: LiveData<String> = showDeleteFragmentController

    private val paymentMethodDeletedController = MutableLiveData<Unit>()
    val paymentMethodDeletedObserver: LiveData<Unit> = paymentMethodDeletedController

    private var isInEditMode = false

    init {
        purchaseInteractor.getSavedCards()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.postValue(LoadingState.LOADING)
            }
            .map { cards->
                cards.filterIsInstance<SavedCardModel>()
            }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    if (it.isNotEmpty()){
                        paymentMethodsController.value = it
                    } else {
                        isInEditMode = false
                        isInEditModeController.value = isInEditMode
                        noPaymentMethodsController.value = true
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        purchaseInteractor.getDeletedCardSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {bindingId->
                    val paymentMethods = paymentMethodsController.value?.filter { it.id != bindingId }
                    if (paymentMethods?.isNullOrEmpty() == false){
                        paymentMethodsController.value = paymentMethods
                        paymentMethodDeletedController.value = Unit
                    } else{
                        isInEditMode = false
                        isInEditModeController.value = isInEditMode
                        noPaymentMethodsController.value = true
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick(){
        if (isInEditMode){
            isInEditMode = false
            isInEditModeController.value = isInEditMode
        } else {
            closeController.value = Unit
        }
    }

    fun onEditClick(){
        isInEditMode = true
        isInEditModeController.value = isInEditMode
    }

    fun onConfirmClick(){
        isInEditMode = false
        isInEditModeController.value = isInEditMode
    }

    fun onDeleteClick(paymentMethod: SavedCardModel){
        showDeleteFragmentController.value = paymentMethod.id
    }

    fun contactMasterOnline(){
        close()
        ScreenManager.showBottomScreen(ChatFragment.newInstance(chatInteractor.getMasterOnlineCase()))
    }

}