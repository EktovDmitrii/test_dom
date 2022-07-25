package com.custom.rgs_android_dom.ui.client.personal_data

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.view_states.PersonalDataViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.ClientFragment
import com.custom.rgs_android_dom.ui.client.personal_data.delete_client.DeleteClientFragment
import com.custom.rgs_android_dom.ui.client.personal_data.edit.EditPersonalDataFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PersonalDataViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val personalDataController = MutableLiveData<PersonalDataViewState>()
    val personalDataObserver: LiveData<PersonalDataViewState> = personalDataController

    init {
        clientInteractor.getClientUpdatedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    personalDataController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getPersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    personalDataController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick() {
        ScreenManager.showBottomScreen(ClientFragment())
    }

    fun onEditClick() {
        ScreenManager.showScreen(EditPersonalDataFragment())
    }

    fun onDeleteClientClick(fragmentManager: FragmentManager) {
        clientInteractor.getAllActiveOrders()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT

                    val deleteClientFragment = DeleteClientFragment.newInstance(it)
                    deleteClientFragment.show(fragmentManager, deleteClientFragment.TAG)
                },
                onError = {
                    logException(this, it)
                    handleNetworkException(it)
                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)
    }
}
