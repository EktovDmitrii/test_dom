package com.custom.rgs_android_dom.ui.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.view_states.ClientShortViewState
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.PersonalDataFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ClientViewModel(
    private val clientInteractor: ClientInteractor,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val clientShortViewStateController = MutableLiveData<ClientShortViewState>()

    val clientShortViewStateObserver: LiveData<ClientShortViewState> =
        clientShortViewStateController

    init {
        clientInteractor.subscribeClientUpdateSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    clientShortViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)


        clientInteractor.getClient()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    clientShortViewStateController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

    }

    fun onLogoutClick() {
        registrationInteractor.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onPersonalDataClick(){
        closeController.value = Unit
        ScreenManager.showScreen(PersonalDataFragment())
    }

}