package com.custom.rgs_android_dom.domain.policies

import android.util.Log
import com.custom.rgs_android_dom.domain.policies.models.PolicyModel
import com.custom.rgs_android_dom.domain.repositories.PoliciesRepository
import com.custom.rgs_android_dom.ui.policies.add.PolicyDialogModel
import com.custom.rgs_android_dom.ui.policies.insurant.InsurantViewState
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class PoliciesInteractor(val policiesRepository: PoliciesRepository) {

    private val insurantViewStateSubject = PublishSubject.create<InsurantViewState>()
    private var insurantViewState = InsurantViewState(
        firstName = "",
        lastName = "",
        middleName = "",
        birthday = "",
        policy = ""
    )

    fun getPolicies(): Single<List<PolicyModel>> {
        return policiesRepository.getPolicies()
    }

    fun policyChanged(policy: String) {
        Log.d("Syrgashev", "policy: $policy")
        insurantViewState = insurantViewState.copy(policy = policy)
        checkNextEnabled()
        insurantViewStateSubject.onNext(insurantViewState)
    }

    fun firstNameChanged(firstName: String) {
        Log.d("Syrgashev", "firstName: $firstName")

        insurantViewState = insurantViewState.copy(firstName = firstName)
        checkNextEnabled()
        insurantViewStateSubject.onNext(insurantViewState)
    }

    fun lastNameChanged(lastName: String) {
        Log.d("Syrgashev", "lastName: $lastName")

        insurantViewState = insurantViewState.copy(lastName = lastName)
        checkNextEnabled()
        insurantViewStateSubject.onNext(insurantViewState)
    }

    fun middleNameChanged(middleName: String) {
        Log.d("Syrgashev", "middleName: $middleName")

        insurantViewState = insurantViewState.copy(middleName = middleName)
        checkNextEnabled()
        insurantViewStateSubject.onNext(insurantViewState)
    }

    fun birthdayChanged(birthday: String) {
        Log.d("Syrgashev", "birthday: $birthday")

        insurantViewState = insurantViewState.copy(birthday = birthday)
        checkNextEnabled()
        insurantViewStateSubject.onNext(insurantViewState)
    }

    fun getInsurantViewStateSubject(): PublishSubject<InsurantViewState> {
        return insurantViewStateSubject
    }
    fun findPolicySingle(): Single<PolicyDialogModel> {
        return policiesRepository.findPolicySingle(insurantViewState.policy)
    }
    private fun checkNextEnabled() {
        insurantViewState = if (insurantViewState.firstName.isNotEmpty() &&
            insurantViewState.lastName.isNotEmpty() &&
            insurantViewState.birthday.isNotEmpty()
        ) {
            insurantViewState.copy(isNextEnabled = true)
        } else {
            insurantViewState.copy(isNextEnabled = false)
        }
    }

}