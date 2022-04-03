package com.custom.rgs_android_dom.ui.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CaseModel
import com.custom.rgs_android_dom.domain.chat.models.ClientCasesModel
import com.custom.rgs_android_dom.domain.registration.RegistrationInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chats.chat.ChatFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatsViewModel(
    private val chatInteractor: ChatInteractor,
    private val registrationInteractor: RegistrationInteractor
) : BaseViewModel() {

    private val casesController = MutableLiveData<ClientCasesModel>()
    val casesObserver: LiveData<ClientCasesModel> = casesController

    init {

        chatInteractor.getCasesFlowable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    casesController.value = it
                },
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        chatInteractor.loadCases()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            )
            .addTo(dataCompositeDisposable)

        registrationInteractor.getLogoutSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    close()
                },
                onError = {
                    close()
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onCaseClick(case: CaseModel){
        val chatFragment = ChatFragment.newInstance(case)
        ScreenManager.showBottomScreen(chatFragment)
    }

}