package com.custom.rgs_android_dom.ui.registration.fill_client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.view_states.FillClientViewState
import com.custom.rgs_android_dom.domain.client.ValidateClientException
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class RegistrationFillClientViewModel(
    private val phone: String,
    private val clientInteractor: ClientInteractor
) : BaseViewModel() {

    private var fillClientViewStateController = MutableLiveData<FillClientViewState>()
    val fillClientViewStateObserver: LiveData<FillClientViewState> = fillClientViewStateController

    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController

    private val validateExceptionController = MutableLiveData<ValidateClientException>()
    val validateExceptionObserver: LiveData<ValidateClientException> = validateExceptionController

    fun init(){
        clientInteractor.fillClientStateSubject.hide()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    fillClientViewStateController.value = it
                },
                onError = {
                    //todo обработка глобальных ошибок
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.validateSubject.hide()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    isSaveTextViewEnabledController.value = it
                },
                onError = {
                    //todo обработка глобальных ошибок
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onKnowAgentCodeClick(){
        clientInteractor.onKnowAgentCodeClick()

    }

    fun onSkipClick(){
        closeController.value = Unit
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onSaveClick(){
        clientInteractor.updateClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    onCloseClick()
                },
                onError = {
                    when(it){
                        is ValidateClientException -> {
                            validateExceptionController.value = it
                            loadingStateController.value = LoadingState.CONTENT
                        }
                        else -> {
                            loadingStateController.value = LoadingState.ERROR
                        }
                    }
                }
            ).addTo(dataCompositeDisposable)

    }

    fun onNameChanged(name: String){
        clientInteractor.onNameChanged(name)
    }

    fun onSurnameChanged(surname: String){
        clientInteractor.onSurnameChanged(surname)
    }

    fun onGenderSelected(gender: Gender){
        clientInteractor.onGenderSelected(gender)
    }

    fun onBirthdayChanged(birthdayString: String){
        clientInteractor.onBirthdayChanged(birthdayString)
    }

    fun onAgentCodeChanged(agentCode: String){
        clientInteractor.onAgentCodeChanged(agentCode)
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        clientInteractor.onAgentPhoneChanged(agentPhone, isMaskFilled)
    }

}