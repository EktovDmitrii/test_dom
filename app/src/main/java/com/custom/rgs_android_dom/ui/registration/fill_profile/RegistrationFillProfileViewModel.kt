package com.custom.rgs_android_dom.ui.registration.fill_profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.utils.tryParseDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate

class RegistrationFillProfileViewModel(
    private val phone: String,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    companion object {
        private val MIN_DATE = LocalDate.now().minusYears(16).plusDays(1)
        private val MAX_DATE = LocalDate.parse("1900-01-01")
    }

    init {
        subscribeLogoutSUbject()
    }

    private val isAgentInfoLinearLayoutVisibleControler = MutableLiveData<Boolean>()
    private val knowAgentCodeTextController = MutableLiveData<String>()
    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    private val birthdayErrorController = MutableLiveData<String>()
    private val agentPhoneErrorController = MutableLiveData<String>()
    private val agentCodeErrorController = MutableLiveData<String>()
    private val resetBirthdayEditTextStateController = MutableLiveData<Unit>()
    private val resetAgentCodeEditTextStateController = MutableLiveData<Unit>()
    private val resetAgentPhoneEditTextStateController = MutableLiveData<Unit>()

    val isAgentInfoLinearLayoutVisibleObserver: LiveData<Boolean> = isAgentInfoLinearLayoutVisibleControler
    val knowAgentCodeTextObserver: LiveData<String> = knowAgentCodeTextController
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController
    val birthdayErrorObserver: LiveData<String> = birthdayErrorController
    val agentPhoneErrorObserver: LiveData<String> = agentPhoneErrorController
    val agentCodeErrorObserver: LiveData<String> = agentCodeErrorController
    val resetBirthdayEditTextStateObserver: LiveData<Unit> = resetBirthdayEditTextStateController
    val resetAgentCodeEditTextStateObserver: LiveData<Unit> = resetAgentCodeEditTextStateController
    val resetAgentPhoneEditTextStateObserver: LiveData<Unit> = resetAgentPhoneEditTextStateController

    private var name: String? = null
    private var surname: String? = null
    private var birthday: LocalDate? = null
    private var gender: Gender? = null
    private var agentCode: String? = null
    private var agentPhone: String? = null

    private var isAgentPhoneCorrect: Boolean = false

    fun onKnowAgentCodeClick(){
        var isAgentInfoLinearLayoutVisible = isAgentInfoLinearLayoutVisibleControler.value ?: false
        isAgentInfoLinearLayoutVisible = !isAgentInfoLinearLayoutVisible

        val knowAgentCodeText = if (isAgentInfoLinearLayoutVisible) "Свернуть информацию об агенте"
            else "Знаю код агента"

        isAgentInfoLinearLayoutVisibleControler.value = isAgentInfoLinearLayoutVisible
        knowAgentCodeTextController.value = knowAgentCodeText
    }

    fun onSkipClick(){
        closeController.value = Unit
    }

    fun onCloseClick(){
        //closeController.value = Unit
        registrationInteractor.logout().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                Log.d("MyLog", "Logged out")
            }
    }

    fun onSaveClick(){
        updateProfile()
    }

    fun onNameChanged(name: String){
        if (name.isNotEmpty()){
            this.name = name
        } else {
            this.name = null
        }
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onSurnameChanged(surname: String){
        if (surname.isNotEmpty()){
            this.surname = surname
        } else {
            this.surname = null
        }
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onGenderSelected(gender: Gender){
        this.gender = gender
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onBirthdayChanged(birthdayString: String, isMaskFilled: Boolean){
        when {
            birthdayString.isEmpty() -> {
                birthday = null
            }
            isMaskFilled -> {
                birthday = birthdayString.tryParseDate({
                    birthday = null
                })

                birthday?.let {birthday->
                    if (isBirthdayValid(birthday)){
                        resetBirthdayEditTextStateController.value = Unit
                    }
                }

            }
            else -> {
                birthday = null
            }
        }
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onAgentCodeChanged(agentCode: String){
        if (agentCode.isEmpty()){
            this.agentCode = null
        } else {
            if (this.agentCode == null){
                resetAgentCodeEditTextStateController.value = Unit
            }
            this.agentCode = agentCode
        }
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        when {
            agentPhone.isEmpty() -> {
                this.agentPhone = null
            }
            else -> {
                if (this.agentPhone == null){
                    resetAgentPhoneEditTextStateController.value = Unit
                }
                this.agentPhone = agentPhone
            }
        }
        isAgentPhoneCorrect = isMaskFilled

        if (isAgentPhoneCorrect){
            resetAgentPhoneEditTextStateController.value = Unit
        }

        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    private fun areNeededFieldsFilled(): Boolean {
        if (name != null || surname != null || gender != null || birthday != null || agentCode !=null && agentPhone != null && isAgentPhoneCorrect){
            return true
        }
        return false
    }

    private fun updateProfile(){
        birthday?.let {birthday->
            if (!isBirthdayValid(birthday)){
                birthdayErrorController.value = "Некорректная дата рождения"
                return
            }
        }

        agentCode?.let { agentCode->
            if (agentCode.length < 12){
                agentCodeErrorController.value = "Неверный код агента"
                return
            }
        }

        if (agentCode != null && agentPhone == null || agentCode != null && agentPhone != null && !isAgentPhoneCorrect){
            agentPhoneErrorController.value = "Укажите телефон агента"
            return
        }

        if (agentCode == null && agentPhone != null && !isAgentPhoneCorrect){
            agentCodeErrorController.value = "Укажите код агента"
            return
        }

        registrationInteractor.updateProfile(phone, name, surname, birthday, gender, agentCode, agentPhone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .doOnSuccess { loadingStateController.value = LoadingState.CONTENT }
            .doOnError { loadingStateController.value = LoadingState.ERROR }
            .subscribeBy(
                onSuccess = {
                    ScreenManager.closeScope(REGISTRATION)
                },
                onError = {

                }
            ).addTo(dataCompositeDisposable)
    }

    private fun subscribeLogoutSUbject() {
        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            )
    }

    private fun isBirthdayValid(birthday: LocalDate): Boolean {
        return !(birthday.isAfter(MIN_DATE) || birthday.isBefore(MAX_DATE))
    }

}