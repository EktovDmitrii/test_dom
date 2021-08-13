package com.custom.rgs_android_dom.domain.profile

import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.profile.models.Gender
import com.custom.rgs_android_dom.domain.registration.ProfileField
import com.custom.rgs_android_dom.domain.registration.ProfileViewState
import com.custom.rgs_android_dom.domain.registration.ValidateProfileException
import com.custom.rgs_android_dom.utils.tryParseDate
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import org.joda.time.LocalDate

class ProfileInteractor(private val registrationRepository: RegistrationRepository) {

    private val MIN_DATE = LocalDate.now().minusYears(16).plusDays(-1)
    private val MAX_DATE = LocalDate.parse("1900-01-01")

    var profileStateSubject = BehaviorRelay.create<ProfileViewState>()
    var validateSubject = BehaviorRelay.create<Boolean>()

    var profileViewState: ProfileViewState = ProfileViewState(registrationRepository.getCurrentPhone())

    fun updateProfile(
        name: String?,
        surname: String?,
        birthday: LocalDate?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Single<Boolean> {
        return registrationRepository.updateProfile(name, surname, birthday, gender, agentCode, agentPhone)
    }

    fun onKnowAgentCodeClick(){
        profileViewState = profileViewState.copy(isOpenCodeAgendFields = !profileViewState.isOpenCodeAgendFields)
        profileStateSubject.accept(profileViewState)
    }

    private fun validateProfileState(){
        validateSubject.accept(profileViewState.name != null ||
                profileViewState.surname != null ||
                profileViewState.gender != null ||
                profileViewState.birthday != null ||
                profileViewState.agentCode?.length == 12 ||
                isAgentPhoneCorrect)
    }

    fun updateProfile(): Single<Boolean> {
        profileViewState.birthday?.let {birthday->
            if (!isBirthdayValid(birthday)) {
                return Single.error(
                    ValidateProfileException(
                        ProfileField.BIRTHDATE,
                        "Некорректная дата рождения"
                    )
                )
            }
        }

        if (profileViewState.agentCode != null && profileViewState.agentPhone == null || profileViewState.agentCode != null && profileViewState.agentPhone != null && isAgentPhoneCorrect){
            return Single.error(ValidateProfileException(ProfileField.AGENTPHONE, "Укажите телефон агента"))
        }

        if (profileViewState.agentCode == null && isAgentPhoneCorrect){
            return Single.error(ValidateProfileException(ProfileField.AGENTCODE, "Укажите код агента"))
        }

        return updateProfile(profileViewState.name, profileViewState.surname, profileViewState.birthday, profileViewState.gender, profileViewState.agentCode, profileViewState.agentPhone)
    }

    fun onNameChanged(name: String) {
        profileViewState = profileViewState.onNameChanged(name)
        validateProfileState()
    }

    fun onSurnameChanged(surname: String) {
        if (surname.isNotEmpty()){
            profileViewState = profileViewState.copy(surname = surname)
        } else {
            profileViewState = profileViewState.copy(surname = null)
        }
        validateProfileState()
    }

    fun onGenderSelected(gender: Gender){
        profileViewState = profileViewState.copy(gender = gender)
        validateProfileState()
    }

    fun onBirthdayChanged(birthdayString: String, isMaskFilled: Boolean){
        var birthday : LocalDate? = null
        when {
            birthdayString.isEmpty() -> {
                birthday = null
            }
            isMaskFilled -> {
                birthday = birthdayString.tryParseDate({
                    birthday = null
                })
            }
            else -> {
                birthday = null
            }
        }
        profileViewState = profileViewState.copy(birthday = birthday)
        validateProfileState()
    }

    fun onAgentCodeChanged(agentCode: String){
        if (agentCode.isEmpty()){
            profileViewState = profileViewState.copy(agentCode = null)
        } else {
            profileViewState = profileViewState.copy(agentCode = agentCode)
        }
        validateProfileState()
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        when {
            agentPhone.isNullOrEmpty() -> {
                profileViewState = profileViewState.copy(agentPhone = null)
            }
            else -> {
                profileViewState = profileViewState.copy(agentPhone = agentPhone, agentPhoneValid = isMaskFilled)
            }
        }
        validateProfileState()
    }

    private fun isBirthdayValid(birthday: LocalDate): Boolean {
        return !(birthday.isAfter(MIN_DATE) || birthday.isBefore(
            MAX_DATE
        ))
    }

    val isAgentPhoneCorrect = profileViewState.agentPhoneValid && profileViewState.agentPhone != null

}