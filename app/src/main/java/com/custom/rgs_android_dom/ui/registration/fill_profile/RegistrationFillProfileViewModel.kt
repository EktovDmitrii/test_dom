package com.custom.rgs_android_dom.ui.registration.fill_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.navigation.REGISTRATION
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.getAge
import com.custom.rgs_android_dom.utils.safeLet
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
        private const val MIN_AGE = 16
    }

    private val isAgentInfoLinearLayoutVisibleControler = MutableLiveData<Boolean>()
    private val knowAgentCodeTextController = MutableLiveData<String>()
    private val isSaveTextViewEnabledController = MutableLiveData<Boolean>()
    private val birthdayErrorController = MutableLiveData<String>()
    private val agentPhoneErrorController = MutableLiveData<String>()

    val isAgentInfoLinearLayoutVisibleObserver: LiveData<Boolean> = isAgentInfoLinearLayoutVisibleControler
    val knowAgentCodeTextObserver: LiveData<String> = knowAgentCodeTextController
    val isSaveTextViewEnabledObserver: LiveData<Boolean> = isSaveTextViewEnabledController
    val birthdayErrorObserver: LiveData<String> = birthdayErrorController
    val agentPhoneErrorObserver: LiveData<String> = agentPhoneErrorController

    private var name: String = ""
    private var surname: String = ""
    private var birthday: LocalDate? = null
    private var gender: Gender? = null
    private var agentCode: String? = null
    private var agentPhone: String? = null

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
        closeController.value = Unit
    }

    fun onSaveClick(){
        updateProfile()
    }

    fun onNameChanged(name: String){
        this.name = name
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onSurnameChanged(surname: String){
        this.surname = surname
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
                val date = birthdayString.tryParseDate({
                    birthdayErrorController.value = "Некорректная дата рождения"
                    birthday = null
                })
                date?.let {
                    if (getAge(it) >= MIN_AGE) {
                        birthday = it
                    } else {
                        birthdayErrorController.value = "Некорректная дата рождения"
                        birthday = null
                    }
                }
            }
            else -> {
                birthday = null
                birthdayErrorController.value = "Некорректная дата рождения"
            }
        }
        isSaveTextViewEnabledController.value = areNeededFieldsFilled()
    }

    fun onAgentCodeChanged(agentCode: String){
        this.agentCode = if (agentCode.length < 12) null else agentCode
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        when {
            agentPhone.isEmpty() -> {
                this.agentPhone = null
            }
            isMaskFilled -> {
                this.agentPhone = agentPhone
            }
            else -> {
                this.agentPhone = null
                agentPhoneErrorController.value = "Некорректный номер телефона"
            }
        }
    }

    private fun areNeededFieldsFilled(): Boolean {
        if (name.isEmpty() || surname.isEmpty() || gender == null || birthday == null){
            return false
        }
        return true
    }

    private fun updateProfile(){
        if (agentCode.isNullOrEmpty() || agentPhone.isNullOrEmpty()){
            agentCode = null
            agentPhone = null
        }

        safeLet(birthday, gender){birthday, gender->
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
    }

}