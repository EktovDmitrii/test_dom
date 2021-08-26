package com.custom.rgs_android_dom.domain.client

import com.custom.rgs_android_dom.data.repositories.client.ClientRepository
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepository
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.mappers.ClientShortViewStateMapper
import com.custom.rgs_android_dom.domain.client.view_states.ClientShortViewState
import com.custom.rgs_android_dom.domain.client.view_states.FillClientViewState
import com.custom.rgs_android_dom.utils.*
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.LocalDateTime

class ClientInteractor(
    private val clientRepository: ClientRepository,
    private val registrationRepository: RegistrationRepository,
    private val countriesRepository: CountriesRepository
) {

    private val MIN_DATE = LocalDateTime.now().minusYears(16).plusDays(-1)
    private val MAX_DATE = LocalDateTime.parse("1900-01-01")

    var fillClientStateSubject = BehaviorRelay.create<FillClientViewState>()
    var validateSubject = BehaviorRelay.create<Boolean>()

    private var fillClientViewState: FillClientViewState =
        FillClientViewState(registrationRepository.getCurrentPhone())

    fun onKnowAgentCodeClick() {
        fillClientViewState =
            fillClientViewState.copy(isOpenCodeAgendFields = !fillClientViewState.isOpenCodeAgendFields)
        fillClientStateSubject.accept(fillClientViewState)
    }

    fun updateClient(): Completable {
        fillClientViewState.birthday?.let { birthday ->
            if (!isBirthdayValid(birthday)) {
                return Completable.error(
                    ValidateClientException(
                        ProfileField.BIRTHDATE,
                        "Некорректная дата рождения"
                    )
                )
            }
        }

        if (fillClientViewState.agentCode != null && fillClientViewState.agentPhone == null || fillClientViewState.agentCode != null && fillClientViewState.agentPhone != null && !isAgentPhoneCorrect()) {

            return Completable.error(
                ValidateClientException(
                    ProfileField.AGENTPHONE,
                    "Укажите телефон агента"
                )
            )
        }

        if (fillClientViewState.agentCode == null && isAgentPhoneCorrect()) {
            return Completable.error(
                ValidateClientException(
                    ProfileField.AGENTCODE,
                    "Укажите код агента"
                )
            )
        }

        return updateClient(
            firstName = fillClientViewState.name,
            lastName = fillClientViewState.surname,
            birthday = fillClientViewState.birthday,
            gender = fillClientViewState.gender,
            agentCode = fillClientViewState.agentCode,
            agentPhone = fillClientViewState.agentPhone
        )
    }

    fun onNameChanged(name: String) {
        fillClientViewState = fillClientViewState.onNameChanged(name)
        validateProfileState()
    }

    fun onSurnameChanged(surname: String) {
        if (surname.isNotEmpty()) {
            fillClientViewState = fillClientViewState.copy(surname = surname)
        } else {
            fillClientViewState = fillClientViewState.copy(surname = null)
        }
        validateProfileState()
    }

    fun onGenderSelected(gender: Gender) {
        fillClientViewState = fillClientViewState.copy(gender = gender)
        validateProfileState()
    }

    fun onBirthdayChanged(birthdayString: String, isMaskFilled: Boolean) {
        var birthday: LocalDateTime? = null
        when {
            birthdayString.isEmpty() -> {
                birthday = null
            }
            isMaskFilled -> {
                val birthdayWithTimezone = "${birthdayString.tryParseDate()}T00:00:00.000Z"
                birthday = birthdayWithTimezone.tryParseLocalDateTime({
                    logException(this, it)
                    birthday = null
                }, format = PATTERN_DATE_TIME_MILLIS)
            }
            else -> {
                birthday = null
            }
        }
        fillClientViewState = fillClientViewState.copy(birthday = birthday)
        validateProfileState()
    }

    fun onAgentCodeChanged(agentCode: String) {
        if (agentCode.isEmpty()) {
            fillClientViewState = fillClientViewState.copy(agentCode = null)
        } else {
            fillClientViewState = fillClientViewState.copy(agentCode = agentCode)
        }
        validateProfileState()
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean) {
        when {
            agentPhone.isNullOrEmpty() -> {
                fillClientViewState = fillClientViewState.copy(agentPhone = null)
            }
            else -> {
                fillClientViewState = fillClientViewState.copy(
                    agentPhone = agentPhone,
                    agentPhoneValid = isMaskFilled
                )
            }
        }
        validateProfileState()
    }

    fun getClient(): Single<ClientShortViewState> {
        return clientRepository.getClient().map {
            ClientShortViewStateMapper.from(it)
        }.doOnSuccess {
            CashHelper.loadAndSaveClient()
        }
    }


    private fun isBirthdayValid(birthday: LocalDateTime): Boolean {
        return !(birthday.isAfter(MIN_DATE) || birthday.isBefore(
            MAX_DATE
        ))
    }

    private fun isAgentPhoneCorrect(): Boolean {
        return fillClientViewState.agentPhoneValid && fillClientViewState.agentPhone != null
    }

    private fun validateProfileState() {
        validateSubject.accept(
            fillClientViewState.name != null ||
                    fillClientViewState.surname != null ||
                    fillClientViewState.gender != null ||
                    fillClientViewState.birthday != null ||
                    fillClientViewState.agentCode != null ||
                    isAgentPhoneCorrect()
        )
    }

    private fun updateClient(
        firstName: String? = null,
        lastName: String? = null,
        middleName: String? = null,
        birthday: LocalDateTime? = null,
        gender: Gender? = null,
        agentCode: String? = null,
        agentPhone: String? = null,
        docNumber: String? = null,
        docSerial: String? = null,
        phone: String? = null,
        secondPhone: String? = null,
        email: String? = null,
    ): Completable {
        return clientRepository.updateClient(firstName, lastName, middleName, birthday, gender, agentCode, agentPhone, docNumber, docSerial, phone, secondPhone, email)
            .doOnComplete { fillClientStateSubject.accept(fillClientViewState) }
    }

    fun subscribeClientUpdateSubject(): Observable<ClientShortViewState> {
        return clientRepository.getClientUpdatedSubject()
            .map {
                ClientShortViewStateMapper.from(it)
            }

    }

}