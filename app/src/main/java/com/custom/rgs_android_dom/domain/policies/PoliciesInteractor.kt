package com.custom.rgs_android_dom.domain.policies

import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.domain.client.exceptions.ValidateFieldModel
import com.custom.rgs_android_dom.domain.policies.models.*
import com.custom.rgs_android_dom.domain.repositories.ClientRepository
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewState
import com.custom.rgs_android_dom.utils.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime

class PoliciesInteractor(
    private val policiesRepository: PoliciesRepository,
    private val clientRepository: ClientRepository,
    private val clientSharedPreferences: ClientSharedPreferences
) {

    companion object {
        private val MIN_DATE = LocalDateTime.now()
        private val MAX_DATE = LocalDateTime.parse("1900-01-01")
    }

    private val insurantViewStateSubject = PublishSubject.create<InsurantViewState>()
    private val policySubject = PublishSubject.create<String>()

    private var insurantViewState = InsurantViewState()
    private var policy: String = ""

    fun policyChanged(policy: String) {
        this.policy = policy
        policiesRepository.onPolicyChange(policy)
        policySubject.onNext(policy)
    }

    fun firstNameChanged(firstName: String, isMaskFilled: Boolean) {
        insurantViewState = insurantViewState.copy(firstName = firstName)
        checkNextEnabled(isMaskFilled)
    }

    fun lastNameChanged(lastName: String, isMaskFilled: Boolean) {
        insurantViewState = insurantViewState.copy(lastName = lastName)
        checkNextEnabled(isMaskFilled)
    }

    fun middleNameChanged(middleName: String, isMaskFilled: Boolean) {
        insurantViewState = insurantViewState.copy(middleName = middleName)
        checkNextEnabled(isMaskFilled)
    }

    fun birthdayChanged(birthday: String, isMaskFilled: Boolean) {
        insurantViewState = insurantViewState.copy(birthday = birthday)
        checkNextEnabled(isMaskFilled)
    }

    fun getInsurantViewStateSubject(isMaskFilled: Boolean): PublishSubject<InsurantViewState> {
        checkNextEnabled(isMaskFilled)
        return insurantViewStateSubject
    }

    fun getPolicySubject(): PublishSubject<String> {
        return policySubject
    }

    fun bindPolicy(): Single<Any> {
        val errorsValidate = ArrayList<ValidateFieldModel>()
        var birthday: DateTime?
        if (insurantViewState.birthday.isNotEmpty()) {
            /*val birthdayWithTimezone = "${insurantViewState.birthday.tryParseDate()}T00:00:00.000Z"
            birthday = birthdayWithTimezone.tryParseLocalDateTime({
                logException(this, it)
                birthday = null
            }, format = PATTERN_DATE_TIME_MILLIS)*/
            birthday = insurantViewState.birthday.tryParseDateToDateTimeUTCWithOffset(0)

            if (birthday == null || birthday != null && !isBirthdayValid(birthday!!)) {
                errorsValidate.add(
                    ValidateFieldModel(
                        ClientField.BIRTHDATE,
                        "Проверьте, правильно ли введена дата рождения"
                    )
                )
            }

            if (errorsValidate.isNotEmpty()) {
                return Single.just(SpecificValidateClientExceptions(errorsValidate))
            } else {
                insurantViewState = insurantViewState.copy(isValidationPassed = true)
                insurantViewStateSubject.onNext(insurantViewState)
            }
        }
        return policiesRepository.bindPolicy(insurantViewState)
    }

    fun getPolicyDialogSubject(): Observable<PolicyDialogModel> {
        return policiesRepository.getPolicyDialogSubject()
    }

    fun newDialog(policyDialogModel: PolicyDialogModel) {
        policiesRepository.newDialog(policyDialogModel)
    }

    fun promptSavePersonalData(save: Boolean) {
        policiesRepository.promptSavePersonalData(save)
    }

    fun getPromptSaveSubject(): Observable<Boolean> {
        return policiesRepository.getPromptSaveSubject()
    }

    fun savePersonalData(boundModel: BoundPolicyDialogModel): Completable {
        policiesRepository.newDialog(PolicyDialogModel(showPrompt = ShowPromptModel.Loading))
        var birthday: DateTime?
        val birthdayWithTimezone = boundModel.contractClientBirthDate
        birthday = birthdayWithTimezone.tryParseDateTime({
            logException(this, it)
            birthday = null
        }, format = PATTERN_DATE_TIME_MILLIS)

        birthday = birthday?.withZone(DateTimeZone.forOffsetHours(3))

        val client = clientSharedPreferences.getClient()
        val avatar = clientRepository.getUserDetails().blockingGet().avatarUrl

        return clientRepository.updateClient(
            firstName = boundModel.contractClientFirstName,
            lastName = boundModel.contractClientLastName,
            middleName = boundModel.contractClientMiddleName,
            birthday = birthday,
            gender = client?.gender,
            phone = client?.phone,
            email = client?.contacts?.find { it.type == "email" }?.contact ?: "",
            avatar = avatar
        )
    }

    fun getPoliciesSingle(): Single<List<PolicyViewholderModel>> {
        return policiesRepository.getPoliciesSingle().map { list ->
            val newList: MutableList<PolicyViewholderModel> = list.toMutableList()
            val indexOfDivider = list.indexOfFirst { it.expiresAt?.isBeforeNow == true }
            if (indexOfDivider != -1) {
                newList.add(indexOfDivider, PolicyDividerModel("Архив"))
            }
            newList
        }
    }

    private fun checkNextEnabled(isMaskFilled: Boolean) {
        insurantViewState = if (
            insurantViewState.firstName.isNotEmpty() &&
            insurantViewState.lastName.isNotEmpty() &&
            insurantViewState.middleName.isNotEmpty() &&
            insurantViewState.birthday.isNotEmpty() &&
            isMaskFilled
        ) {
            insurantViewState.copy(isNextEnabled = true)
        } else {
            insurantViewState.copy(isNextEnabled = false)
        }
        insurantViewStateSubject.onNext(insurantViewState)
    }

    private fun isBirthdayValid(birthday: DateTime): Boolean {
        return !(birthday.toLocalDateTime().isAfter( MIN_DATE ) || birthday.toLocalDateTime().isBefore( MAX_DATE ))
    }

    fun getPolicy(contractId: String): Single<PolicyModel> {
        return policiesRepository.getPolicySingle(contractId)
    }

    fun getPolicyDialogModel(): PolicyDialogModel?{
        return policiesRepository.getPolicyDialogModel()
    }

}
