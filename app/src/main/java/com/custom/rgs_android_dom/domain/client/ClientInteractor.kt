package com.custom.rgs_android_dom.domain.client

import com.custom.rgs_android_dom.data.repositories.files.FilesRepositoryImpl.Companion.STORE_AVATARS
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateFieldModel
import com.custom.rgs_android_dom.domain.client.mappers.AgentMapper
import com.custom.rgs_android_dom.domain.client.mappers.ClientShortViewStateMapper
import com.custom.rgs_android_dom.domain.client.mappers.EditPersonalDataViewStateMapper
import com.custom.rgs_android_dom.domain.client.mappers.PersonalDataMapper
import com.custom.rgs_android_dom.domain.client.models.*
import com.custom.rgs_android_dom.domain.client.view_states.*
import com.custom.rgs_android_dom.domain.repositories.*
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.utils.*
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.io.File

class
ClientInteractor(
    private val clientRepository: ClientRepository,
    private val registrationRepository: RegistrationRepository,
    private val catalogRepository: CatalogRepository,
    private val policiesRepository: PoliciesRepository,
    private val filesRepository: FilesRepository
) {

    companion object {
        private val MIN_DATE = LocalDateTime.now()
        private val MAX_DATE = LocalDateTime.parse("1900-01-01")

        private const val DOC_SERIAL_LENGTH = 4
        private const val DOC_NUMBER_LENGTH = 6

        private const val ASSIGN_TYPE_REG = "reg"
        private const val ASSIGN_TYPE_PROFILE = "profile"
    }

    var fillClientStateSubject = BehaviorRelay.create<FillClientViewState>()
    var validateSubject = BehaviorRelay.create<Boolean>()
    var editAgentStateSubject = BehaviorRelay.create<EditAgentViewState>()

    private var fillClientViewState: FillClientViewState =
        FillClientViewState(registrationRepository.getCurrentPhone())
    private var editPersonalDataViewState = EditPersonalDataViewState()
    private var editAgentViewState = EditAgentViewState()

    fun getOrdersHistory(): Single<List<Order>> {
        return clientRepository.getOrders(1000, 0)
            .flatMap {orders->
                Single.just(sortOrderHistory(orders.filter { it.status != OrderStatus.DRAFT }))
            }
    }

    private fun sortOrderHistory(orders: List<Order>): List<Order> {
        val activeOrders = mutableListOf<Order>()
        val otherOrders = mutableListOf<Order>()
        orders.forEach {
            if (it.status == OrderStatus.ACTIVE) activeOrders.add(it) else otherOrders.add(it)
        }
        return mutableListOf<Order>().apply {
            addAll(activeOrders.sortedByDescending {
                LocalDate.parse(
                    it.deliveryDate,
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ")
                )
            })
            addAll(otherOrders.sortedByDescending {
                LocalDate.parse(
                    it.deliveryDate,
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ")
                )
            })
        }
    }

    fun onKnowAgentCodeClick() {
        fillClientViewState =
            fillClientViewState.copy(isOpenCodeAgendFields = !fillClientViewState.isOpenCodeAgendFields)
        fillClientStateSubject.accept(fillClientViewState)
    }

    fun updateNewClient(): Completable {

        val errorsValidate = ArrayList<ValidateFieldModel>()

        if (fillClientViewState.surname?.trim()?.isEmpty() == true) {
            errorsValidate.add(ValidateFieldModel(ClientField.LASTNAME, ""))
        }

        if (fillClientViewState.name?.trim()?.isEmpty() == true) {
            errorsValidate.add(ValidateFieldModel(ClientField.FIRSTNAME, ""))
        }

        var birthday: DateTime? = null
        fillClientViewState.birthday?.let { birthdayString ->

            /*val birthdayWithTimezone = "${birthdayString.tryParseDate()}T00:00:00.000Z"
            birthday = birthdayWithTimezone.tryParseDateTime({
                logException(this, it)
                birthday = null
            }, format = PATTERN_DATE_TIME_MILLIS)*/
            birthday = birthdayString.tryParseDateToDateTimeUTCWithOffset(0)

            if (birthday == null || birthday != null && !isBirthdayValid(birthday!!)) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.BIRTHDATE,
                        "Некорректная дата рождения"
                    )
                )
            } else if (birthday != null && !isAdult(birthday!!)) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.BIRTHDATE,
                        TranslationInteractor.getTranslation("app.profile.add.birthday.service_unavailable")
                    )
                )
            } else { }
        }

        if (fillClientViewState.agentCode == null && fillClientViewState.agentPhone != null) {
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTCODE, "Укажите код агента"))
        }

        if (fillClientViewState.agentPhone != null) {
            if (!fillClientViewState.agentPhoneValid) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.AGENTPHONE,
                        "Укажите телефон агента"
                    )
                )
            }
        }

        if (fillClientViewState.agentPhone == null && fillClientViewState.agentCode != null) {
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, "Укажите телефон агента"))
        }

        if (errorsValidate.isNotEmpty()) {
            return Completable.error(SpecificValidateClientExceptions(errorsValidate))
        }

        val clientCompletable = updateClient(
            firstName = fillClientViewState.name?.trim(),
            lastName = fillClientViewState.surname?.trim(),
            birthday = birthday,
            gender = fillClientViewState.gender,
        )

        val agentCompletable = if (fillClientViewState.agentCode != null) {
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
        return Single.zip(
            clientRepository.getClient(),
            clientRepository.getUserDetails()
        ) { clientModel, userDetailsModel ->
            ClientShortViewStateMapper.from(clientModel, userDetailsModel)
        }.doOnSuccess {
            CacheHelper.loadAndSaveClient()
        }
    }

    fun getClientModelSingle(): Single<ClientModel> {
        return clientRepository.getClient()
    }

    fun getEditPersonalDataViewState(): Single<EditPersonalDataViewState> {
        return Single.zip(
            clientRepository.getClient(),
            catalogRepository.getClientProducts(null),
            policiesRepository.getPoliciesSingle()
        ) { clientModel, clientProducts, policies ->
            editPersonalDataViewState =
                EditPersonalDataViewStateMapper.from(clientModel, clientProducts, policies)
            return@zip editPersonalDataViewState
        }.doOnSuccess {
            CacheHelper.loadAndSaveClient()
        }
    }

    fun subscribeClientUpdateSubject(): Observable<ClientShortViewState> {
        return clientRepository.getClientUpdatedSubject().flatMap { clientModel ->
            clientRepository.getUserDetails().flatMapObservable { userDetailsModel ->
                Observable.fromCallable {
                    ClientShortViewStateMapper.from(clientModel, userDetailsModel)
                }
            }
        }
    }

    fun getClientUpdatedSubject(): Observable<PersonalDataViewState> {
        return clientRepository.getClientUpdatedSubject().flatMap { clientModel ->
            clientRepository.getUserDetails().flatMapObservable { userDetailsModel ->
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
        return Single.zip(
            clientRepository.getClient(),
            clientRepository.getUserDetails()
        ) { clientModel, userDetailsModel ->
            PersonalDataMapper.from(clientModel, userDetailsModel)
        }
    }

    fun onEditPersonalDataLastNameChanged(lastName: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(lastName = lastName)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataFirstNameChanged(firstName: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(firstName = firstName)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataMiddleNameChanged(middleName: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(middleName = middleName)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataBirthdayChanged(birthday: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(birthday = birthday)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataGenderChanged(gender: Gender) {
        editPersonalDataViewState = editPersonalDataViewState.copy(gender = gender)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataPhoneChanged(phone: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(phone = phone)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataDocSerialChanged(docSerial: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(docSerial = docSerial)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataDocNumberChanged(docNumber: String) {
        editPersonalDataViewState = editPersonalDataViewState.copy(docNumber = docNumber)
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataSecondPhoneChanged(secondPhone: String, isMaskFilled: Boolean) {
        editPersonalDataViewState = editPersonalDataViewState.copy(
            secondPhone = secondPhone,
            isSecondPhoneValid = isMaskFilled,
            wasSecondPhoneEdited = true
        )
        validateEditPersonalDataState()
    }

    fun onEditPersonalDataEmailChanged(email: String) {
        editPersonalDataViewState =
            editPersonalDataViewState.copy(email = email, wasEmailEdited = true)
        validateEditPersonalDataState()
    }

    fun savePersonalData(): Completable {

        val errorsValidate = ArrayList<ValidateFieldModel>()
        if (!editPersonalDataViewState.isLastNameSaved && editPersonalDataViewState.lastName.isNotEmpty()) {
            if (editPersonalDataViewState.lastName.trim().isEmpty()) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.LASTNAME,
                        TranslationInteractor.getTranslation("app.profile.client_edit.last_name_error_label")
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isFirstNameSaved && editPersonalDataViewState.firstName.isNotEmpty()) {
            if (editPersonalDataViewState.firstName.trim().isEmpty()) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.FIRSTNAME,
                        TranslationInteractor.getTranslation("app.profile.client_edit.first_name_error_label")
                    )
                )
            }
        }

        if (!editPersonalDataViewState.isMiddleNameSaved && editPersonalDataViewState.middleName.isNotEmpty()) {
            if (editPersonalDataViewState.middleName.trim().isEmpty()) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.MIDDLENAME,
                        TranslationInteractor.getTranslation("app.profile.client_edit.middle_name_error_label")
                    )
                )
            }
        }

        var birthday: DateTime? = null
        if (editPersonalDataViewState.birthday.isNotEmpty()) {
            /*val birthdayWithTimezone = "${editPersonalDataViewState.birthday.tryParseDate()}T00:00:00.000Z"
            birthday = birthdayWithTimezone.tryParseDateTime({
                logException(this, it)
                birthday = null
            }, format = PATTERN_DATE_TIME_MILLIS)*/
            birthday = editPersonalDataViewState.birthday.tryParseDateToDateTimeUTCWithOffset(0)
            if (birthday == null || !isBirthdayValid(birthday)) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.BIRTHDATE,
                        TranslationInteractor.getTranslation("app.profile.client_edit.date_birth_error_label")
                    )
                )
            } else if (!isAdult(birthday)) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.BIRTHDATE,
                        TranslationInteractor.getTranslation("app.profile.client_edit.service_unavailable_date_birth_error_label")
                    )
                )
            }

            //birthday = birthday?.withZone(DateTimeZone.forOffsetHours(3))
        }

        if (editPersonalDataViewState.hasProducts && !editPersonalDataViewState.isDocSerialSaved && editPersonalDataViewState.docSerial.isNotEmpty()
            || !editPersonalDataViewState.hasProducts && editPersonalDataViewState.docSerial.isNotEmpty()
        ) {
            if (editPersonalDataViewState.docSerial.trim().length != DOC_SERIAL_LENGTH) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.DOC_SERIAL,
                        TranslationInteractor.getTranslation("app.profile.client_edit.passport_series_error_label")
                    )
                )
            }
            if (editPersonalDataViewState.docNumber.trim().length != DOC_NUMBER_LENGTH) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.DOC_NUMBER,
                        TranslationInteractor.getTranslation("app.profile.client_edit.passport_number_error_label")
                    )
                )
            }
        }

        if (editPersonalDataViewState.hasProducts && !editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()
            || !editPersonalDataViewState.hasProducts && editPersonalDataViewState.docNumber.isNotEmpty()
        ) {
            if (editPersonalDataViewState.docNumber.trim().length != DOC_NUMBER_LENGTH) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.DOC_NUMBER,
                        TranslationInteractor.getTranslation("app.profile.client_edit.passport_number_error_label")
                    )
                )
            }
            if (editPersonalDataViewState.docSerial.trim().length != DOC_SERIAL_LENGTH) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.DOC_SERIAL,
                        TranslationInteractor.getTranslation("app.profile.client_edit.passport_series_error_label")
                    )
                )
            }
        }

        if (editPersonalDataViewState.wasSecondPhoneEdited && editPersonalDataViewState.secondPhone.isNotEmpty() && !editPersonalDataViewState.isSecondPhoneValid) {
            errorsValidate.add(
                ValidateFieldModel(
                    ClientField.SECOND_PHONE,
                    TranslationInteractor.getTranslation("app.profile.client_edit.additional_number_error_label")
                )
            )
        }

        if (editPersonalDataViewState.wasEmailEdited && editPersonalDataViewState.email.isNotEmpty() && !editPersonalDataViewState.email.isValidEmail()) {
            errorsValidate.add(
                ValidateFieldModel(
                    ClientField.EMAIL,
                    TranslationInteractor.getTranslation("app.profile.client_edit.email_error_label")
                )
            )
        }

        if (errorsValidate.isNotEmpty()) {
            return Completable.error(SpecificValidateClientExceptions(errorsValidate))
        }

        val updatePassportCompletable =
            if (!editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()) {
                postPassport(
                    editPersonalDataViewState.docSerial,
                    editPersonalDataViewState.docNumber
                )
            } else if (editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()) {
                updatePassport(
                    editPersonalDataViewState.docId,
                    editPersonalDataViewState.docSerial,
                    editPersonalDataViewState.docNumber
                )
            } else {
                Completable.complete()
            }

        val updatePhoneCompletable =
            if (editPersonalDataViewState.wasSecondPhoneEdited && editPersonalDataViewState.secondPhone.isNotEmpty()) {
                if (editPersonalDataViewState.isSecondPhoneSaved) {
                    clientRepository.updateSecondPhone(
                        editPersonalDataViewState.secondPhone,
                        editPersonalDataViewState.secondPhoneId
                    )
                } else {
                    clientRepository.saveSecondPhone(editPersonalDataViewState.secondPhone)
                }
            } else {
                Completable.complete()
            }

        val deleteIds = arrayListOf<String>()
        if (editPersonalDataViewState.wasEmailEdited
            && editPersonalDataViewState.email.isEmpty()
            && editPersonalDataViewState.emailId.isNotEmpty()
        ) {
            deleteIds.add(editPersonalDataViewState.emailId)
        }
        if (editPersonalDataViewState.wasSecondPhoneEdited
            && editPersonalDataViewState.secondPhone.isEmpty()
            && editPersonalDataViewState.secondPhoneId.isNotEmpty()
        ) {
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
        return Completable.concatArray(
            updatePassportCompletable,
            updatePhoneCompletable,
            updateClientCompletable,
            deleteCompletable
        )
    }

    fun onEditAgentCodeChanged(agentCode: String) {
        editAgentViewState = editAgentViewState.copy(agentCode = agentCode)
        validateAgentState()
    }

    fun onEditAgentPhoneChanged(agentPhone: String, isMaskFilled: Boolean) {
        editAgentViewState =
            editAgentViewState.copy(agentPhone = agentPhone, isAgentPhoneValid = isMaskFilled)
        validateAgentState()
    }

    fun getAgent(): Single<AgentViewState> {
        return clientRepository.getClient().map {
            AgentMapper.from(it)
        }
    }

    fun updateAgent(): Completable {
        val errorsValidate = ArrayList<ValidateFieldModel>()
        if (editAgentViewState.agentCode.trim().isEmpty()) {
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTCODE, TranslationInteractor.getTranslation("app.agent_info_edit.agent_phone_error_label")))
        }

        if (editAgentViewState.agentPhone.trim().isEmpty()) {
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, TranslationInteractor.getTranslation("app.agent_info_edit.agent_phone_error_label")))
        }

        if (editAgentViewState.agentPhone.isNotEmpty() && !editAgentViewState.isAgentPhoneValid) {
            errorsValidate.add(ValidateFieldModel(ClientField.AGENTPHONE, TranslationInteractor.getTranslation("app.agent_info_edit.agent_phone_error_label")))
        }

        if (errorsValidate.isNotEmpty()) {
            return Completable.error(SpecificValidateClientExceptions(errorsValidate))
        }

        return clientRepository.assignAgent(
            editAgentViewState.agentCode,
            editAgentViewState.agentPhone,
            ASSIGN_TYPE_PROFILE
        )
    }

    fun requestEditAgent(): Completable {
        return clientRepository.requestEditAgent()
    }

    fun isEditAgentRequested(): Single<Boolean>{
        return clientRepository.getRequestEditAgentTasks().map {
            it.find { it.status == RequestEditAgentStatus.OPEN } != null
        }
    }

    fun getEditAgentRequestedSubject(): PublishSubject<Boolean>{
        return clientRepository.getEditAgentRequestedSubject()
    }

    fun requestEditClient(): Completable {
        return clientRepository.requestEditClient()
    }

    fun getEditClientRequestedSubject(): PublishSubject<Boolean> {
        return clientRepository.getEditClientRequestedSubject()
    }

    fun isEditClientRequested(): Single<Boolean>{
        return clientRepository.getRequestEditClientTasks().map {
            it.find { it.status == RequestEditClientStatus.OPEN } != null
        }
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
            }.flatMapCompletable { clientModel ->
                return@flatMapCompletable updateClient(
                    firstName = clientModel.firstName,
                    lastName = clientModel.lastName,
                    middleName = clientModel.middleName,
                    birthday = clientModel.birthDate,
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
        }.flatMapCompletable { clientModel ->
            return@flatMapCompletable updateClient(
                firstName = clientModel.firstName,
                lastName = clientModel.lastName,
                middleName = clientModel.middleName,
                birthday = clientModel.birthDate,
                gender = clientModel.gender,
                phone = clientModel.phone,
                email = clientModel.contacts?.find { it.type == "email" }?.contact,
                avatar = null
            )
        }
    }

    private fun isBirthdayValid(birthday: DateTime): Boolean {
        return !(birthday.toLocalDateTime().isAfter(MIN_DATE) || birthday.toLocalDateTime().isBefore(
            MAX_DATE
        ))
    }

    private fun isAdult(birthday: DateTime): Boolean {
        val now = DateTime.now()
        return if ((now.year - birthday.year) >= 16) {
            !(now.monthOfYear == birthday.monthOfYear && now.dayOfMonth == birthday.dayOfMonth)
        } else {
            false
        }
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

    private fun validateEditPersonalDataState() {
        var isSaveTextViewEnabled = false
        if (editPersonalDataViewState.hasProducts) {
            if (!editPersonalDataViewState.isFirstNameSaved && editPersonalDataViewState.firstName.isNotEmpty()
                || !editPersonalDataViewState.isLastNameSaved && editPersonalDataViewState.lastName.isNotEmpty()
                || !editPersonalDataViewState.isMiddleNameSaved && editPersonalDataViewState.middleName.isNotEmpty()
                || !editPersonalDataViewState.isBirthdaySaved && editPersonalDataViewState.birthday.isNotEmpty()
                || !editPersonalDataViewState.isGenderSaved && editPersonalDataViewState.gender != null
                || !editPersonalDataViewState.isPhoneSaved && editPersonalDataViewState.phone.isNotEmpty()
                || !editPersonalDataViewState.isDocSerialSaved && editPersonalDataViewState.docSerial.isNotEmpty()
                || !editPersonalDataViewState.isDocNumberSaved && editPersonalDataViewState.docNumber.isNotEmpty()
                || editPersonalDataViewState.wasSecondPhoneEdited
                || editPersonalDataViewState.wasEmailEdited
            ) {
                isSaveTextViewEnabled = true
            }
        } else {
            if (editPersonalDataViewState.firstName.isNotEmpty()
                || editPersonalDataViewState.lastName.isNotEmpty()
                || editPersonalDataViewState.middleName.isNotEmpty()
                || editPersonalDataViewState.birthday.isNotEmpty()
                || editPersonalDataViewState.gender != null
                || editPersonalDataViewState.phone.isNotEmpty()
                || editPersonalDataViewState.docSerial.isNotEmpty()
                || editPersonalDataViewState.docNumber.isNotEmpty()
                || editPersonalDataViewState.wasSecondPhoneEdited
                || editPersonalDataViewState.wasEmailEdited
            ) {
                isSaveTextViewEnabled = true
            }
        }

        validateSubject.accept(isSaveTextViewEnabled)
    }

    private fun updateClient(
        firstName: String? = null,
        lastName: String? = null,
        middleName: String? = null,
        birthday: DateTime? = null,
        gender: Gender? = null,
        phone: String? = null,
        email: String? = null,
        avatar: String? = null
    ): Completable {
        return clientRepository.updateClient(
            firstName,
            lastName,
            middleName,
            birthday,
            gender,
            phone,
            email,
            avatar
        )
            .doOnComplete { fillClientStateSubject.accept(fillClientViewState) }
    }

    private fun validateAgentState() {
        var isSaveTextViewEnabled =
            editAgentViewState.agentCode.isNotEmpty() || editAgentViewState.agentPhone.isNotEmpty()
        validateSubject.accept(isSaveTextViewEnabled)
    }

    private fun postPassport(serial: String, number: String): Completable {
        return clientRepository.postPassport(serial, number)
    }

    private fun updatePassport(id: String, serial: String, number: String): Completable {
        return clientRepository.updatePassport(id, serial, number)
    }

    fun finishAuth() {
        registrationRepository.finishAuth()
    }

    fun getOrder(orderId: String): Single<Order> {
        return clientRepository.getOrder(orderId)
    }

    fun cancelOrder(order: Order): Completable {
        return clientRepository.cancelOrder(order.id)
    }

    fun getOrderCancelledSubject(): PublishSubject<Unit> {
        return clientRepository.getOrderCancelledSubject()
    }

    fun getCancelledTasks(orderId: String): Single<List<CancelledTaskModel>> {
        return clientRepository.getCancelledTasks(orderId)
    }

    fun saveFCMToken(token: String, deviceId: String): Completable {
        return if (registrationRepository.isAuthorized()){
            clientRepository.saveFCMToken(token, deviceId)
        } else {
            Completable.complete()
        }
    }

    fun updateNotificationChannel(notificationChannel: NotificationChannelInfo): Single<ClientModel> {
        return clientRepository.updateNotificationChannel(notificationChannel)
    }

    fun getAllActiveOrders(): Single<List<Order>> {
        return clientRepository.getAllOrders(1000, 0)
            .flatMap { orders->
                Single.just(sortOrderHistory(orders.filter { it.status != OrderStatus.CANCELLED && it.status != OrderStatus.RESOLVED && it.status != OrderStatus.DRAFT}))
            }
    }

}
