package com.custom.rgs_android_dom.domain.client

import com.custom.rgs_android_dom.data.repositories.files.FilesRepositoryImpl.Companion.STORE_AVATARS
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateFieldModel
import com.custom.rgs_android_dom.domain.client.mappers.AgentMapper
import com.custom.rgs_android_dom.domain.client.models.Gender
import com.custom.rgs_android_dom.domain.client.mappers.ClientShortViewStateMapper
import com.custom.rgs_android_dom.domain.client.mappers.EditPersonalDataViewStateMapper
import com.custom.rgs_android_dom.domain.client.mappers.PersonalDataMapper
import com.custom.rgs_android_dom.domain.client.view_states.*
import com.custom.rgs_android_dom.domain.repositories.ClientRepository
import com.custom.rgs_android_dom.domain.repositories.FilesRepository
import com.custom.rgs_android_dom.domain.repositories.RegistrationRepository
import com.custom.rgs_android_dom.utils.*
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.LocalDateTime
import java.io.File

class ClientInteractor(
    private val clientRepository: ClientRepository,
    private val registrationRepository: RegistrationRepository,
    private val filesRepository: FilesRepository
) {

    companion object {
        private val MIN_DATE = LocalDateTime.now().minusYears(16).plusDays(-1)
        private val MAX_DATE = LocalDateTime.parse("1900-01-01")

        private val DOC_SERIAL_LENGTH = 4
        private val DOC_NUMBER_LENGTH = 6

        private const val ASSIGN_TYPE_REG = "reg"
        private const val ASSIGN_TYPE_PROFILE = "profile"
    }

    var fillClientStateSubject = BehaviorRelay.create<FillClientViewState>()
    var validateSubject = BehaviorRelay.create<Boolean>()
    var editAgentStateSubject = BehaviorRelay.create<EditAgentViewState>()

    private var fillClientViewState: FillClientViewState = FillClientViewState(registrationRepository.getCurrentPhone())
    private var editPersonalDataViewState = EditPersonalDataViewState()
    private var editAgentViewState = EditAgentViewState()

    //todo
    fun saveText(text: Boolean) {
        clientRepository.saveTextToAgent(text)
    }

    fun onKnowAgentCodeClick() {
        fillClientViewState =
            fillClientViewState.copy(isOpenCodeAgendFields = !fillClientViewState.isOpenCodeAgendFields)
        fillClientStateSubject.accept(fillClientViewState)
    }

    fun updateNewClient(): Completable {

        val errorsValidate = ArrayList<ValidateFieldModel>()

        if (fillClientViewState.surname?.trim()?.isEmpty() == true){
            errorsValidate.add(ValidateFieldModel(ClientField.LASTNAME, ""))
        }

        if (fillClientViewState.name?.trim()?.isEmpty() == true){
            errorsValidate.add(ValidateFieldModel(ClientField.FIRSTNAME, ""))
        }

        var birthday: LocalDateTime? = null
        fillClientViewState.birthday?.let { birthdayString ->

            val birthdayWithTimezone = "${birthdayString.tryParseDate()}T00:00:00.000Z"
            birthday = birthdayWithTimezone.tryParseLocalDateTime({
                logException(this, it)
                birthday = null
            }, format = PATTERN_DATE_TIME_MILLIS)

            if (birthday == null || birthday != null && !isBirthdayValid(birthday!!)) {
                errorsValidate.add(ValidateFieldModel(ClientField.BIRTHDATE, "Некорректная дата рождения"))
            }
        }

        if (fillClientViewState.agentCode == null && fillClientViewState.agentPhone != null) {
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTCODE, "Укажите код агента"))
        }

        if (fillClientViewState.agentPhone != null){
            if (!fillClientViewState.agentPhoneValid){
                errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, "Укажите телефон агента"))
            }
        }

        if (fillClientViewState.agentPhone == null && fillClientViewState.agentCode != null){
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, "Укажите телефон агента"))
        }

        if (errorsValidate.isNotEmpty()){
            return Completable.error(SpecificValidateClientExceptions(errorsValidate))
        }

        val clientCompletable = updateClient(
            firstName = fillClientViewState.name?.trim(),
            lastName = fillClientViewState.surname?.trim(),
            birthday = birthday,
            gender = fillClientViewState.gender,
        )

        val agentCompletable = if (fillClientViewState.agentCode != null){
            clientRepository.assignAgent(
                code = fillClientViewState.agentCode?.trim() ?: "",
                phone = fillClientViewState.agentPhone ?: "",
                assignType = ASSIGN_TYPE_REG
            )
        } else {
            Completable.complete()
        }
        return Completable.concatArray(clientCompletable, agentCompletable)
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
        fillClientViewState = when {
            agentPhone.isNullOrEmpty() -> {
                fillClientViewState.copy(agentPhone = null)
            }
            else -> {
                fillClientViewState.copy(
                    agentPhone = agentPhone,
                    agentPhoneValid = isMaskFilled
                )
            }
        }
        validateProfileState()
    }

    fun getClient(): Single<ClientShortViewState> {
        return Single.zip(clientRepository.getClient(), clientRepository.getUserDetails()){clientModel, userDetailsModel ->
            ClientShortViewStateMapper.from(clientModel, userDetailsModel)
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
        return clientRepository.getClientUpdatedSubject().flatMap { clientModel->
            clientRepository.getUserDetails().flatMapObservable {  userDetailsModel->
                Observable.fromCallable {
                    ClientShortViewStateMapper.from(clientModel, userDetailsModel)
                }
            }
        }
    }

    fun getClientUpdatedSubject(): Observable<PersonalDataViewState> {
        return clientRepository.getClientUpdatedSubject().flatMap { clientModel->
            clientRepository.getUserDetails().flatMapObservable {  userDetailsModel->
                Observable.fromCallable {
                    PersonalDataMapper.from(clientModel, userDetailsModel)
                }
            }
        }
    }

    fun agentUpdatedSubject(): Observable<AgentViewState> {
        return clientRepository.getClientUpdatedSubject()
            .map {
                AgentMapper.from(it)
            }
    }

    fun getPersonalData(): Single<PersonalDataViewState> {
        return Single.zip(clientRepository.getClient(), clientRepository.getUserDetails()){clientModel, userDetailsModel ->
            PersonalDataMapper.from(clientModel, userDetailsModel)
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
        editPersonalDataViewState = editPersonalDataViewState.copy(secondPhone = secondPhone, isSecondPhoneValid = isMaskFilled, wasSecondPhoneEdited = true)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataEmailChanged(email: String){
        editPersonalDataViewState = editPersonalDataViewState.copy(email = email, wasEmailEdited = true)
        validateEditPersonalDataState()
    }

    fun savePersonalData(): Completable {

        val errorsValidate = ArrayList<ValidateFieldModel>()
        if (!editPersonalDataViewState.isLastNameSaved && editPersonalDataViewState.lastName.isNotEmpty()){
            if (editPersonalDataViewState.lastName.trim().isEmpty()){
                errorsValidate.add(ValidateFieldModel(ClientField.LASTNAME, "Проверьте, правильно ли введена фамилия"))
            }
        }

        if (!editPersonalDataViewState.isFirstNameSaved && editPersonalDataViewState.firstName.isNotEmpty()){
            if (editPersonalDataViewState.firstName.trim().isEmpty()){
                errorsValidate.add(ValidateFieldModel(ClientField.FIRSTNAME, "Проверьте, правильно ли введено имя"))
            }
        }

        if (!editPersonalDataViewState.isMiddleNameSaved && editPersonalDataViewState.middleName.isNotEmpty()){
            if (editPersonalDataViewState.middleName.trim().isEmpty()){
                errorsValidate.add(ValidateFieldModel(ClientField.MIDDLENAME, "Проверьте, правильно ли введено отчество"))
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
                errorsValidate.add(ValidateFieldModel(ClientField.BIRTHDATE, "Проверьте, правильно ли введена дата рождения"))
            }
        }

        if (!editPersonalDataViewState.isDocSerialSaved && editPersonalDataViewState.docSerial.isNotEmpty()){
            if (editPersonalDataViewState.docSerial.trim().length != DOC_SERIAL_LENGTH){
                errorsValidate.add(ValidateFieldModel(ClientField.DOC_SERIAL, "Проверьте, правильно ли введена серия паспорта"))
            }
            if (editPersonalDataViewState.docNumber.trim().length != DOC_NUMBER_LENGTH){
                errorsValidate.add(ValidateFieldModel(ClientField.DOC_NUMBER, "Проверьте, правильно ли введён номер паспорта"))
            }
        }

        if (!editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()){
            if (editPersonalDataViewState.docNumber.trim().length != DOC_NUMBER_LENGTH){
                errorsValidate.add(ValidateFieldModel(ClientField.DOC_NUMBER, "Проверьте, правильно ли введён номер паспорта"))
            }
            if (editPersonalDataViewState.docSerial.trim().length != DOC_SERIAL_LENGTH){
                errorsValidate.add(ValidateFieldModel(ClientField.DOC_SERIAL, "Проверьте, правильно ли введена серия паспорта"))
            }
        }

        if (editPersonalDataViewState.wasSecondPhoneEdited && editPersonalDataViewState.secondPhone.isNotEmpty() && !editPersonalDataViewState.isSecondPhoneValid){
            errorsValidate.add(ValidateFieldModel(ClientField.SECOND_PHONE, "Проверьте, правильно ли введён номер телефона"))
        }

        if (editPersonalDataViewState.wasEmailEdited && editPersonalDataViewState.email.isNotEmpty() && !editPersonalDataViewState.email.isValidEmail()){
            errorsValidate.add(ValidateFieldModel(ClientField.EMAIL, "Проверьте, правильно ли введён email"))
        }

        if (errorsValidate.isNotEmpty()){
            return Completable.error(SpecificValidateClientExceptions(errorsValidate))
        }

        val updatePassportCompletable = if (!editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()){
            updatePassport(editPersonalDataViewState.docSerial, editPersonalDataViewState.docNumber)
        } else {
            Completable.complete()
        }

        var updatePhoneCompletable = if (editPersonalDataViewState.wasSecondPhoneEdited && editPersonalDataViewState.secondPhone.isNotEmpty()){
            if (editPersonalDataViewState.isSecondPhoneSaved){
                clientRepository.updateSecondPhone(editPersonalDataViewState.secondPhone, editPersonalDataViewState.secondPhoneId)
            } else {
                clientRepository.saveSecondPhone(editPersonalDataViewState.secondPhone)
            }
        } else {
            Completable.complete()
        }

        val deleteIds = arrayListOf<String>()
        if (editPersonalDataViewState.wasEmailEdited
                && editPersonalDataViewState.email.isEmpty()
                && editPersonalDataViewState.emailId.isNotEmpty()){
            deleteIds.add(editPersonalDataViewState.emailId)
        }
        if (editPersonalDataViewState.wasSecondPhoneEdited
            && editPersonalDataViewState.secondPhone.isEmpty()
            && editPersonalDataViewState.secondPhoneId.isNotEmpty()){
            deleteIds.add(editPersonalDataViewState.secondPhoneId)
        }

        val deleteCompletable = if (deleteIds.isNotEmpty()) {
            clientRepository.deleteContacts(deleteIds)
        } else {
            Completable.complete()
        }

        val updateClientCompletable = clientRepository.getUserDetails().flatMapCompletable {
            updateClient(
                lastName = if (editPersonalDataViewState.lastName.isNotEmpty()) editPersonalDataViewState.lastName else null,
                firstName = if (editPersonalDataViewState.firstName.isNotEmpty()) editPersonalDataViewState.firstName else null,
                middleName = if (editPersonalDataViewState.middleName.isNotEmpty()) editPersonalDataViewState.middleName else null,
                birthday = birthday,
                gender = editPersonalDataViewState.gender,
                email = if (editPersonalDataViewState.email.isNotEmpty()) editPersonalDataViewState.email else null,
                avatar = it.avatarFileId
            )
        }
        return Completable.concatArray(updatePassportCompletable, updatePhoneCompletable, updateClientCompletable, deleteCompletable)
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
        val errorsValidate = ArrayList<ValidateFieldModel>()
        if (editAgentViewState.agentCode.trim().isEmpty()){
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTCODE, ""))
        }

        if (editAgentViewState.agentPhone.trim().isEmpty()){
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, ""))
        }

        if (editAgentViewState.agentPhone.isNotEmpty() && !editAgentViewState.isAgentPhoneValid){
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, ""))
        }

        if (errorsValidate.isNotEmpty()){
            return Completable.error(SpecificValidateClientExceptions(errorsValidate))
        }

        return clientRepository.assignAgent(editAgentViewState.agentCode, editAgentViewState.agentPhone, ASSIGN_TYPE_PROFILE)
    }

    fun requestEditAgent(): Completable {
        return clientRepository.requestEditAgent()
    }

    fun getEditAgentRequestedSubject(): PublishSubject<Boolean> {
        return clientRepository.getEditAgentRequestedSubject()
    }

    fun requestEditPersonalData(): Completable {
        return clientRepository.requestEditPersonalData()
    }

    fun getEditPersonalDataRequestedSubject(): PublishSubject<Boolean> {
        return clientRepository.getEditPersonalDataRequestedSubject()
    }

    fun loadAndSaveClient(): Completable {
        return clientRepository.loadAndSaveClient()
    }

    fun updateAvatar(avatar: File): Completable {
        var fileId: String? = null
        return filesRepository.putFileToTheStore(avatar, STORE_AVATARS)
            .flatMap {
                fileId = it
                clientRepository.getClient()
            }.flatMapCompletable{ clientModel->
                return@flatMapCompletable updateClient(
                    firstName = clientModel.firstName,
                    lastName = clientModel.lastName,
                    middleName = clientModel.middleName,
                    birthday = clientModel.birthDate?.toLocalDateTime(),
                    gender = clientModel.gender,
                    phone = clientModel.phone,
                    email = clientModel.contacts?.find { it.type == "email" }?.contact,
                    avatar = fileId
                )
            }.doFinally {
                avatar.delete()
            }
    }

    fun deleteAvatar(): Completable {
        return clientRepository.getUserDetails().flatMap {
            filesRepository.deleteFileFromStore(it.avatarFileId ?: "").toSingle {
                true
            }
        }.flatMap {
            clientRepository.getClient()
        }.flatMapCompletable{ clientModel->
            return@flatMapCompletable updateClient(
                firstName = clientModel.firstName,
                lastName = clientModel.lastName,
                middleName = clientModel.middleName,
                birthday = clientModel.birthDate?.toLocalDateTime(),
                gender = clientModel.gender,
                phone = clientModel.phone,
                email = clientModel.contacts?.find { it.type == "email" }?.contact,
                avatar = null
            )
        }
    }

    private fun isBirthdayValid(birthday: LocalDateTime): Boolean {
        return !(birthday.isAfter(MIN_DATE) || birthday.isBefore(
            MAX_DATE
        ))
    }

    private fun validateProfileState() {
        validateSubject.accept(
            fillClientViewState.name != null ||
                    fillClientViewState.surname != null ||
                    fillClientViewState.gender != null ||
                    fillClientViewState.birthday != null ||
                    fillClientViewState.agentCode != null ||
                    fillClientViewState.agentPhoneValid && fillClientViewState.agentPhone != null
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
            || editPersonalDataViewState.wasSecondPhoneEdited
            || editPersonalDataViewState.wasEmailEdited){
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
        phone: String? = null,
        email: String? = null,
        avatar: String? = null
    ): Completable {
        return clientRepository.updateClient(firstName, lastName, middleName, birthday, gender, phone, email, avatar)
            .doOnComplete { fillClientStateSubject.accept(fillClientViewState) }
    }

    private fun validateAgentState(){
        var isSaveTextViewEnabled = editAgentViewState.agentCode.isNotEmpty() || editAgentViewState.agentPhone.isNotEmpty()
        validateSubject.accept(isSaveTextViewEnabled)
    }

    private fun updatePassport(serial: String, number: String): Completable {
        return clientRepository.updatePassport(serial, number)
    }

}