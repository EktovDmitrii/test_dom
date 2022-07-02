package com.custom.rgs_android_dom.ui.client.personal_data.delete_client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.data_adapters.NetworkException
import com.custom.rgs_android_dom.data.network.toMSDErrorModel
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateFieldModel
import com.custom.rgs_android_dom.domain.client.models.Order
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.registration.fill_client.RegistrationFillClientViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class DeleteClientViewModel(
    private val activeOrders: List<Order>,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val activeOrdersController = MutableLiveData(activeOrders)
    val activeOrdersObserver: LiveData<List<Order>> = activeOrdersController

    fun onConfirmClick(){
        registrationInteractor.deleteClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadingStateController.value = LoadingState.LOADING
            }
            .subscribeBy(
                onComplete = {
                    close()
                },
                onError = {
                    loadingStateController.value = LoadingState.ERROR
                    logException(this, it)
                    if (it is NetworkException) {
                        it.toMSDErrorModel()?.let {
                            networkErrorController.value = TranslationInteractor.getTranslation(it.messageKey)
                        }
                    } else {
                        handleNetworkException(it)
                    }
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCancelClick(){
        close()
    }

}