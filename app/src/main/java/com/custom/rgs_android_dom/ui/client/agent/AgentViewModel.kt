package com.custom.rgs_android_dom.ui.client.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.view_states.AgentViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.agent.edit.EditAgentFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class AgentViewModel(private val clientInteractor: ClientInteractor) : BaseViewModel() {

    private val agentController = MutableLiveData<AgentViewState>()
    val agentObserver: LiveData<AgentViewState> = agentController

    private val editAgentRequestedController = MutableLiveData<Boolean>()
    val editAgentRequestedObserver: LiveData<Boolean> = editAgentRequestedController


    init {
        loadAgent()

        clientInteractor.agentUpdatedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    loadAgent()
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getEditAgentRequestedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    editAgentRequestedController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onEditClick(){
        ScreenManager.showScreen(EditAgentFragment())
    }

    private fun loadAgent(){
        Single.zip(clientInteractor.getAgent(), clientInteractor.isEditAgentRequested()){agent, isEditAgentRequested->
            agentController.postValue(agent)
            if (!agent.isEditAgentButtonVisible){
                editAgentRequestedController.postValue(isEditAgentRequested)
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onError = {
                logException(this, it)
            }
        ).addTo(dataCompositeDisposable)
    }

}