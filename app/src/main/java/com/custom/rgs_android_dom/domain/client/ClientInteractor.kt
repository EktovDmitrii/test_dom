package com.custom.rgs_android_dom.domain.client

import android.util.Log
import com.custom.rgs_android_dom.data.repositories.client.ClientRepository
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepository
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.utils.*
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class ClientInteractor(private val clientRepository: ClientRepository,
                       private val registrationRepository: RegistrationRepository,
                       private val countriesRepository: CountriesRepository
) {

    private val MIN_DATE = LocalDateTime.now().minusYears(16).plusDays(-1)
    private val MAX_DATE = LocalDateTime.parse("1900-01-01")

    var fillClientStateSubject = BehaviorRelay.create<FillClientViewState>()
    var validateSubject = BehaviorRelay.create<Boolean>()

    val clientShortStateSubject = BehaviorRelay.create<ClientShortViewState>()

    private var fillClientViewState: FillClientViewState = FillClientViewState(registrationRepository.getCurrentPhone())
    private var clientShortViewState = ClientShortViewState()

    init {
        subscribeClientUpdateSubject()
    }

    fun onKnowAgentCodeClick(){
        fillClientViewState = fillClientViewState.copy(isOpenCodeAgendFields = !fillClientViewState.isOpenCodeAgendFields)
        fillClientStateSubject.accept(fillClientViewState)
    }

    fun updateClient(): Completable {
        fillClientViewState.birthday?.let { birthday->
            if (!isBirthdayValid(birthday)) {
                return Completable.error(
                    ValidateClientException(
                        ProfileField.BIRTHDATE,
                        "Некорректная дата рождения"
                    )
                )
            }
        }

        if (fillClientViewState.agentCode != null && fillClientViewState.agentPhone == null || fillClientViewState.agentCode != null && fillClientViewState.agentPhone != null && !isAgentPhoneCorrect()){

            return Completable.error(ValidateClientException(ProfileField.AGENTPHONE, "Укажите телефон агента"))
        }

        if (fillClientViewState.agentCode == null && isAgentPhoneCorrect()){
            return Completable.error(ValidateClientException(ProfileField.AGENTCODE, "Укажите код агента"))
        }

        return updateClient(fillClientViewState.name, fillClientViewState.surname, fillClientViewState.birthday, fillClientViewState.gender, fillClientViewState.agentCode, fillClientViewState.agentPhone)
    }

    fun onNameChanged(name: String) {
        fillClientViewState = fillClientViewState.onNameChanged(name)
        validateProfileState()
    }

    fun onSurnameChanged(surname: String) {
        if (surname.isNotEmpty()){
            fillClientViewState = fillClientViewState.copy(surname = surname)
        } else {
            fillClientViewState = fillClientViewState.copy(surname = null)
        }
        validateProfileState()
    }

    fun onGenderSelected(gender: Gender){
        fillClientViewState = fillClientViewState.copy(gender = gender)
        validateProfileState()
    }

    fun onBirthdayChanged(birthdayString: String, isMaskFilled: Boolean){
        var birthday : LocalDateTime? = null
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

    fun onAgentCodeChanged(agentCode: String){
        if (agentCode.isEmpty()){
            fillClientViewState = fillClientViewState.copy(agentCode = null)
        } else {
            fillClientViewState = fillClientViewState.copy(agentCode = agentCode)
        }
        validateProfileState()
    }

    fun onAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        when {
            agentPhone.isNullOrEmpty() -> {
                fillClientViewState = fillClientViewState.copy(agentPhone = null)
            }
            else -> {
                fillClientViewState = fillClientViewState.copy(agentPhone = agentPhone, agentPhoneValid = isMaskFilled)
            }
        }
        validateProfileState()
    }

    fun getClient(): Single<ClientShortViewState>{
        return clientRepository.getClientFromCache().map {
            val phoneMask = countriesRepository.getMaskForPhone(it.phone)

            clientShortViewState = clientShortViewState.copy(
                firstName = it.firstName,
                lastName = it.lastName,
                phone = it.phone.formatPhoneByMask(phoneMask, "#"))

            return@map clientShortViewState
        }.doOnSuccess {
            clientRepository.loadClient().subscribe()
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

    private fun validateProfileState(){
        validateSubject.accept(fillClientViewState.name != null ||
                fillClientViewState.surname != null ||
                fillClientViewState.gender != null ||
                fillClientViewState.birthday != null ||
                fillClientViewState.agentCode != null ||
                isAgentPhoneCorrect())
    }

    private fun updateClient(
        name: String?,
        surname: String?,
        birthday: LocalDateTime?,
        gender: Gender?,
        agentCode: String?,
        agentPhone: String?
    ): Completable {
        return clientRepository.updateClient(name, surname, birthday, gender, agentCode, agentPhone)
            .doOnComplete { fillClientStateSubject.accept(fillClientViewState) }
    }

    private fun subscribeClientUpdateSubject(){
        clientRepository.getClientUpdatedSubject()
            .subscribeBy {
                clientShortViewState = clientShortViewState.copy(firstName = it.firstName, lastName = it.lastName, phone = it.phone)
                clientShortStateSubject.accept(clientShortViewState)
            }
    }

}