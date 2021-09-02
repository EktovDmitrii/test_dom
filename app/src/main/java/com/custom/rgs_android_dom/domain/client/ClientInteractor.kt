package com.custom.rgs_android_dom.domain.client

import com.custom.rgs_android_dom.data.repositories.client.ClientRepository
import com.custom.rgs_android_dom.data.repositories.countries.CountriesRepository
import com.custom.rgs_android_dom.data.repositories.registration.RegistrationRepository
import com.custom.rgs_android_dom.domain.client.mappers.AgentMapper
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateClientException
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.mappers.ClientShortViewStateMapper
import com.custom.rgs_android_dom.domain.client.mappers.EditPersonalDataViewStateMapper
import com.custom.rgs_android_dom.domain.client.mappers.PersonalDataMapper
import com.custom.rgs_android_dom.domain.client.view_states.*
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

    companion object {
        private val MIN_DATE = LocalDateTime.now().minusYears(16).plusDays(-1)
        private val MAX_DATE = LocalDateTime.parse("1900-01-01")

        private val DOC_SERIAL_LENGTH = 4
        private val DOC_NUMBER_LENGTH = 6
    }

    var fillClientStateSubject = BehaviorRelay.create<FillClientViewState>()
    var validateSubject = BehaviorRelay.create<Boolean>()
    var editAgentStateSubject = BehaviorRelay.create<EditAgentViewState>()

    private var fillClientViewState: FillClientViewState = FillClientViewState(registrationRepository.getCurrentPhone())
    private var editPersonalDataViewState = EditPersonalDataViewState()
    private var editAgentViewState = EditAgentViewState()


    fun onKnowAgentCodeClick() {
        fillClientViewState =
            fillClientViewState.copy(isOpenCodeAgendFields = !fillClientViewState.isOpenCodeAgendFields)
        fillClientStateSubject.accept(fillClientViewState)
    }

    fun updateClient(): Completable {

        var birthday: LocalDateTime? = null
        fillClientViewState.birthday?.let { birthdayString ->

            val birthdayWithTimezone = "${birthdayString.tryParseDate()}T00:00:00.000Z"
            birthday = birthdayWithTimezone.tryParseLocalDateTime({
                logException(this, it)
                birthday = null
            }, format = PATTERN_DATE_TIME_MILLIS)

            if (birthday == null || birthday != null && !isBirthdayValid(birthday!!)) {
                return Completable.error(
                    ValidateClientException(
                        ClientField.BIRTHDATE,
                        "Некорректная дата рождения"
                    )
                )
            }
        }

        if (fillClientViewState.agentCode != null && fillClientViewState.agentPhone == null || fillClientViewState.agentCode != null && fillClientViewState.agentPhone != null && !isAgentPhoneCorrect()) {

            return Completable.error(
                ValidateClientException(
                    ClientField.AGENTPHONE,
                    "Укажите телефон агента"
                )
            )
        }

        if (fillClientViewState.agentCode == null && isAgentPhoneCorrect()) {
            return Completable.error(
                ValidateClientException(
                    ClientField.AGENTCODE,
                    "Укажите код агента"
                )
            )
        }

        return updateClient(
            firstName = fillClientViewState.name,
            lastName = fillClientViewState.surname,
            birthday = birthday,
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

    fun onBirthdayChanged(birthdayString: String) {

        fillClientViewState = when {
            birthdayString.isNotEmpty() -> {
                fillClientViewState.copy(birthday = birthdayString)
            }
            else -> {
                fillClientViewState.copy(birthday = null)
            }
        }

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
            CacheHelper.loadAndSaveClient()
        }
    }

    fun getEditPersonalDataViewState(): Single<EditPersonalDataViewState>{
        return clientRepository.getClient().map {
            editPersonalDataViewState = EditPersonalDataViewStateMapper.from(it)
            return@map editPersonalDataViewState
        }.doOnSuccess {
            CacheHelper.loadAndSaveClient()
        }
    }

    fun subscribeClientUpdateSubject(): Observable<ClientShortViewState> {
        return clientRepository.getClientUpdatedSubject()
            .map {
                ClientShortViewStateMapper.from(it)
            }
    }

    fun getClientUpdatedSubject(): Observable<PersonalDataViewState> {
        return clientRepository.getClientUpdatedSubject()
            .map {
                PersonalDataMapper.from(it)
            }
    }

    fun agentUpdatedSubject(): Observable<AgentViewState> {
        return clientRepository.getClientUpdatedSubject()
            .map {
                AgentMapper.from(it)
            }
    }

    fun getPersonalData(): Single<PersonalDataViewState> {
        return clientRepository.getClient().map {
            PersonalDataMapper.from(it)
        }
    }

    fun onEditPersonalDataLastNameChanged(lastName: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(lastName = lastName)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataFirstNameChanged(firstName: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(firstName = firstName)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataMiddleNameChanged(middleName: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(middleName = middleName)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataBirthdayChanged(birthday: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(birthday = birthday)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataGenderChanged(gender: Gender){
        editPersonalDataViewState = editPersonalDataViewState.copy(gender = gender)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataPhoneChanged(phone: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(phone = phone)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataDocSerialChanged(docSerial: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(docSerial = docSerial)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataDocNumberChanged(docNumber: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(docNumber = docNumber)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataSecondPhoneChanged(secondPhone: String, isMaskFilled: Boolean){
        editPersonalDataViewState = editPersonalDataViewState.copy(secondPhone = secondPhone, isSecondPhoneValid = isMaskFilled)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataEmailChanged(email: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(email = email)
        validateEditPersonalDataState()
    }

    fun savePersonalData(): Completable {
        if (!editPersonalDataViewState.isLastNameSaved && editPersonalDataViewState.lastName.isNotEmpty()){
            if (editPersonalDataViewState.lastName.trim().isEmpty()){
                return Completable.error(
                    ValidateClientException(
                        ClientField.LASTNAME,
                        "Проверьте, правильно ли введена фамилия"
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isFirstNameSaved && editPersonalDataViewState.firstName.isNotEmpty()){
            if (editPersonalDataViewState.firstName.trim().isEmpty()){
                return Completable.error(
                    ValidateClientException(
                        ClientField.FIRSTNAME,
                        "Проверьте, правильно ли введено имя"
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isMiddleNameSaved && editPersonalDataViewState.middleName.isNotEmpty()){
            if (editPersonalDataViewState.middleName.trim().isEmpty()){
                return Completable.error(
                    ValidateClientException(
                        ClientField.FIRSTNAME,
                        "Проверьте, правильно ли введено отчество"
                    )
                )
            }
        }

        var birthday: LocalDateTime? = null
        if (editPersonalDataViewState.birthday.isNotEmpty()){
            val birthdayWithTimezone = "${editPersonalDataViewState.birthday.tryParseDate()}T00:00:00.000Z"
            birthday = birthdayWithTimezone.tryParseLocalDateTime({
                logException(this, it)
                birthday = null
            }, format = PATTERN_DATE_TIME_MILLIS)

            if (birthday == null || birthday != null && !isBirthdayValid(birthday!!)) {
                return Completable.error(
                    ValidateClientException(
                        ClientField.BIRTHDATE,
                        "Проверьте, правильно ли введена дата рождения"
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isDocSerialSaved && editPersonalDataViewState.docSerial.isNotEmpty()){
            if (editPersonalDataViewState.docSerial.trim().length != DOC_SERIAL_LENGTH){
                return Completable.error(
                    ValidateClientException(
                        ClientField.DOC_SERIAL,
                        "Проверьте, правильно ли введена серия паспорта"
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()){
            if (editPersonalDataViewState.docNumber.trim().length != DOC_NUMBER_LENGTH){
                return Completable.error(
                    ValidateClientException(
                        ClientField.DOC_NUMBER,
                        "Проверьте, правильно ли введён номер паспорта"
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isSecondPhoneSaved && editPersonalDataViewState.secondPhone.isNotEmpty() && !editPersonalDataViewState.isSecondPhoneValid){
            return Completable.error(
                ValidateClientException(
                    ClientField.SECOND_PHONE,
                    "Проверьте, правильно ли введён номер телефона"
                )
            )
        }

        if (!editPersonalDataViewState.isEmailSaved && editPersonalDataViewState.email.isNotEmpty() && !editPersonalDataViewState.email.isValidEmail()){
            return Completable.error(
                ValidateClientException(
                    ClientField.EMAIL,
                    "Проверьте, правильно ли введён email"
                )
            )
        }
        return clientRepository.getClient().flatMapCompletable { clientModel ->
            updateClient(
                lastName = if (editPersonalDataViewState.lastName.isNotEmpty()) editPersonalDataViewState.lastName else null,
                firstName = if (editPersonalDataViewState.firstName.isNotEmpty()) editPersonalDataViewState.firstName else null,
                middleName = if (editPersonalDataViewState.middleName.isNotEmpty()) editPersonalDataViewState.middleName else null,
                birthday = birthday,
                agentCode = clientModel.agent?.code,
                agentPhone = clientModel.agent?.phone,
                gender = editPersonalDataViewState.gender,
                docSerial = if (editPersonalDataViewState.docSerial.isNotEmpty()) editPersonalDataViewState.docSerial else null,
                docNumber = if (editPersonalDataViewState.docNumber.isNotEmpty()) editPersonalDataViewState.docNumber else null,
                secondPhone = if (editPersonalDataViewState.secondPhone.isNotEmpty()) editPersonalDataViewState.secondPhone else null,
                email = if (editPersonalDataViewState.email.isNotEmpty()) editPersonalDataViewState.email else null
            )
        }
    }

    fun onEditAgentCodeChanged(agentCode: String){
        editAgentViewState = editAgentViewState.copy(agentCode = agentCode)
        validateAgentState()
    }

    fun onEditAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean){
        editAgentViewState = editAgentViewState.copy(agentPhone = agentPhone, isAgentPhoneValid = isMaskFilled)
        validateAgentState()
    }

    fun getAgent(): Single<AgentViewState>{
        return clientRepository.getClient().map {
            AgentMapper.from(it)
        }
    }

    fun updateAgent(): Completable {
        if (editAgentViewState.agentCode.trim().isEmpty()){
            return Completable.error(
                ValidateClientException(
                    ClientField.AGENTCODE,
                    "ЛНР и телефон агента работают в связке, заполните оба поля"
                )
            )
        }

        if (editAgentViewState.agentPhone.trim().isEmpty()){
            return Completable.error(
                ValidateClientException(
                    ClientField.AGENTPHONE,
                    "ЛНР и телефон агента работают в связке, заполните оба поля"
                )
            )
        }

        if (editAgentViewState.agentPhone.isNotEmpty() && !editAgentViewState.isAgentPhoneValid){
            return Completable.error(
                ValidateClientException(
                    ClientField.AGENTPHONE,
                    "Проверьте, правильно ли введён номер телефона"
                )
            )
        }
        return clientRepository.getClient().flatMapCompletable {clientModel->
            updateClient(
                lastName = clientModel.lastName,
                firstName = clientModel.firstName,
                middleName = clientModel.middleName,
                birthday = clientModel.birthDate?.toLocalDateTime(),
                gender = clientModel.gender,
                agentCode = editAgentViewState.agentCode,
                agentPhone = editAgentViewState.agentPhone,
                docSerial = clientModel.docSerial,
                docNumber = if (editPersonalDataViewState.docNumber.isNotEmpty()) editPersonalDataViewState.docNumber else null,
                secondPhone = if (editPersonalDataViewState.secondPhone.isNotEmpty()) editPersonalDataViewState.secondPhone else null,
                email = if (editPersonalDataViewState.email.isNotEmpty()) editPersonalDataViewState.email else null
            )
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

    private fun validateEditPersonalDataState(){
        var isSaveTextViewEnabled = false
        if (!editPersonalDataViewState.isFirstNameSaved && editPersonalDataViewState.firstName.isNotEmpty()
            || !editPersonalDataViewState.isLastNameSaved && editPersonalDataViewState.lastName.isNotEmpty()
            || !editPersonalDataViewState.isMiddleNameSaved && editPersonalDataViewState.middleName.isNotEmpty()
            || !editPersonalDataViewState.isBirthdaySaved && editPersonalDataViewState.birthday.isNotEmpty()
            || !editPersonalDataViewState.isGenderSaved && editPersonalDataViewState.gender != null
            || !editPersonalDataViewState.isPhoneSaved && editPersonalDataViewState.phone.isNotEmpty()
            || !editPersonalDataViewState.isDocSerialSaved && editPersonalDataViewState.docSerial.isNotEmpty()
            || !editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()
            || !editPersonalDataViewState.isSecondPhoneSaved && editPersonalDataViewState.secondPhone.isNotEmpty()
            || !editPersonalDataViewState.isEmailSaved && editPersonalDataViewState.email.isNotEmpty()){
            isSaveTextViewEnabled = true
        }

        validateSubject.accept(isSaveTextViewEnabled)
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


    private fun validateAgentState(){
        var isSaveTextViewEnabled = editAgentViewState.agentCode.isNotEmpty() || editAgentViewState.agentPhone.isNotEmpty()
        validateSubject.accept(isSaveTextViewEnabled)
    }
}