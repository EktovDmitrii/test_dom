package com.custom.rgs_android_dom.ui.policies.insurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.mappers.PoliciesMapper
import com.custom.rgs_android_dom.data.network.toMSDErrorModel
import com.custom.rgs_android_dom.domain.client.exceptions.SpecificValidateClientExceptions
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.domain.policies.models.PolicyDialogModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class InsurantViewModel(private val policiesInteractor: PoliciesInteractor) : BaseViewModel() {

    private val insurantViewStateController = MutableLiveData<InsurantViewState>()
    val insurantViewStateObserver: LiveData<InsurantViewState> = insurantViewStateController

    private val validateExceptionController = MutableLiveData<SpecificValidateClientExceptions>()
    val validateExceptionObserver: LiveData<SpecificValidateClientExceptions> = validateExceptionController


    fun onBackClick() {
        close()
    }

    fun firstNameChanged(firstName: String, isMaskFilled: Boolean) {
        policiesInteractor.firstNameChanged(firstName, isMaskFilled)
    }

    fun lastNameChanged(lastName: String, isMaskFilled: Boolean) {
        policiesInteractor.lastNameChanged(lastName, isMaskFilled)
    }

    fun middleNameChanged(middleName: String, isMaskFilled: Boolean) {
        policiesInteractor.middleNameChanged(middleName, isMaskFilled)
    }

    fun birthdayChanged(birthday: String, isMaskFilled: Boolean) {
        policiesInteractor.birthdayChanged(birthday, isMaskFilled)
    }

    fun onNextClick() {
        policiesInteractor.bindPolicy()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    if( it is PolicyDialogModel ) {
                        policiesInteractor.newDialog(it)
                    } else {
                        validateExceptionController.value = it as SpecificValidateClientExceptions
                    }
                            },
                onError = {
                    if (it.toMSDErrorModel() != null) {
                        policiesInteractor.newDialog(
                            PolicyDialogModel(
                                failureMessage = PoliciesMapper.responseErrorToFailure(
                                    it.toMSDErrorModel()?.defaultMessage
                                )
                            )
                        )
                    } else {
                        logException(this, it)
                    }
                })
            .addTo(dataCompositeDisposable)
    }

    fun requireViewState(isMaskFilled: Boolean) {
        policiesInteractor.getInsurantViewStateSubject(isMaskFilled)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    insurantViewStateController.value = it },
                onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
    }

}