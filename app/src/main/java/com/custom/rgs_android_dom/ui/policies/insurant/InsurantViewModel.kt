package com.custom.rgs_android_dom.ui.policies.insurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.policies.PoliciesInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.ScheduledRunnable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class InsurantViewModel(private val policiesInteractor: PoliciesInteractor) : BaseViewModel() {

    private val insurantViewStateController = MutableLiveData<InsurantViewState>()
    val insurantViewStateObserver: LiveData<InsurantViewState> = insurantViewStateController

    init {
        policiesInteractor.getInsurantViewStateSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {insurantViewStateController.value = it},
                onError = { logException(this, it) })
            .addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        close()
    }

    fun firstNameChanged(firstName: String) {
        policiesInteractor.firstNameChanged(firstName)
    }

    fun lastNameChanged(lastName: String) {
        policiesInteractor.lastNameChanged(lastName)
    }

    fun middleNameChanged(middleName: String) {
        policiesInteractor.middleNameChanged(middleName)
    }

    fun birthdayChanged(birthday: String) {
        policiesInteractor.birthdayChanged(birthday)
    }

    fun onNextClick() {
        policiesInteractor.bindPolicy()
    }

}
