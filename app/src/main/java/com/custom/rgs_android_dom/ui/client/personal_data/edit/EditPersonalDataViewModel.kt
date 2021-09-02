package com.custom.rgs_android_dom.ui.client.personal_data.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateClientException
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.view_states.EditPersonalDataViewState
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.registration.phone.RegistrationPhoneFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class EditPersonalDataViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val editPersonalDataController = MutableLiveData<EditPersonalDataViewState>()
    val editPersonalDataObserver: LiveData<EditPersonalDataViewState> = editPersonalDataController

    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController

    private val validateExceptionController = MutableLiveData<ValidateClientException>()
    val validateExceptionObserver: LiveData<ValidateClientException> = validateExceptionController

    init {
        clientInteractor.getEditPersonalDataViewState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    editPersonalDataController.value = it
                },
                onError = {
                    logException(this, it)
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
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

    }

    fun onLastNameChanged(lastName: String){
        clientInteractor.onEditPersonalDataLastNameChanged(lastName)
    }

    fun onFirstNameChanged(firstName: String){
        clientInteractor.onEditPersonalDataFirstNameChanged(firstName)
    }

    fun onMiddleNameChanged(middleName: String){
        clientInteractor.onEditPersonalDataMiddleNameChanged(middleName)
    }

    fun onBirthdayChanged(birthday: String){
        clientInteractor.onEditPersonalDataBirthdayChanged(birthday)
    }

    fun onGenderChanged(gender: Gender){
        clientInteractor.onEditPersonalDataGenderChanged(gender)
    }

    fun onPhoneChanged(phone: String){
        clientInteractor.onEditPersonalDataPhoneChanged(phone)
    }

    fun onDocSerialChanged(docSerial: String) {
        clientInteractor.onEditPersonalDataDocSerialChanged(docSerial)
    }

    fun onDocNumberChanged(docNumber: String){
        clientInteractor.onEditPersonalDataDocNumberChanged(docNumber)
    }

    fun onSecondPhoneChanged(secondPhone: String, isMaskFilled: Boolean){
        clientInteractor.onEditPersonalDataSecondPhoneChanged(secondPhone, isMaskFilled)
    }

    fun onEmailChanged(email: String){
        clientInteractor.onEditPersonalDataEmailChanged(email)
    }

    fun onSaveClick(){
        clientInteractor.savePersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onComplete = {
                    onBackClick()
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

    fun onBackClick(){
        closeController.value = Unit
    }

}