package com.custom.rgs_android_dom.ui.registration.fill_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.profile.ProfileInteractor
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.ProfileViewState
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.domain.registration.ValidateProfileException
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.tryParseDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate

class RegistrationFillProfileViewModel(
    private val phone: String,
    private val profileInteractor: ProfileInteractor
) : BaseViewModel() {
//
//    private val isAgentInfoLinearLayoutVisibleControler = MutableLiveData<Boolean>()
//    private val knowAgentCodeTextController = MutableLiveData<String>()
//
//    private val birthdayErrorController = MutableLiveData<String>()
//    private val agentPhoneErrorController = MutableLiveData<String>()
//    private val agentCodeErrorController = MutableLiveData<String>()
//    private val resetBirthdayEditTextStateController = MutableLiveData<Unit>()
//    private val resetAgentCodeEditTextStateController = MutableLiveData<Unit>()
//    private val resetAgentPhoneEditTextStateController = MutableLiveData<Unit>()
//
//    val isAgentInfoLinearLayoutVisibleObserver: LiveData<Boolean> = isAgentInfoLinearLayoutVisibleControler
//    val knowAgentCodeTextObserver: LiveData<String> = knowAgentCodeTextController
//
//    val birthdayErrorObserver: LiveData<String> = birthdayErrorController
//    val agentPhoneErrorObserver: LiveData<String> = agentPhoneErrorController
//    val agentCodeErrorObserver: LiveData<String> = agentCodeErrorController
//    val resetBirthdayEditTextStateObserver: LiveData<Unit> = resetBirthdayEditTextStateController
//    val resetAgentCodeEditTextStateObserver: LiveData<Unit> = resetAgentCodeEditTextStateController
//    val resetAgentPhoneEditTextStateObserver: LiveData<Unit> = resetAgentPhoneEditTextStateController

    private var profileViewStateController = MutableLiveData<ProfileViewState>()
    val profileViewStateObserver: LiveData<ProfileViewState> = profileViewStateController

    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController

    private val validateExceptionController = MutableLiveData<ValidateProfileException>()
    val validateExceptionObserver: LiveData<ValidateProfileException> = validateExceptionController

    fun init(){
        profileInteractor.profileStateSubject.hide()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                        profileViewStateController.value = it
                },
                onError = {
                    //todo обработка глобальных ошибок
                }
            ).addTo(dataCompositeDisposable)

        profileInteractor.validateSubject.hide()
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
        profileInteractor.onKnowAgentCodeClick()

    }

    fun onSkipClick(){
        closeController.value = Unit
    }

    fun onCloseClick(){
        closeController.value = Unit
    }

    fun onSaveClick(){
        profileInteractor.updateProfile()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    closeController.value = Unit
                },
                onError = {
                    when(it){
                        is ValidateProfileException -> {
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
        profileInteractor.onNameChanged(name)
    }

    fun onSurnameChanged(surname: String){
        profileInteractor.onSurnameChanged(surname)
    }

    fun onGenderSelected(gender: Gender){
        profileInteractor.onGenderSelected(gender)
    }

    fun onBirthdayChanged(birthdayString: String, isMaskFilled: Boolean){
        profileInteractor.onBirthdayChanged(birthdayString, isMaskFilled)
    }

    fun onAgentCodeChanged(agentCode: String){
        profileInteractor.onAgentCodeChanged(agentCode)
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        profileInteractor.onAgentPhoneChanged(agentPhone, isMaskFilled)
    }

}