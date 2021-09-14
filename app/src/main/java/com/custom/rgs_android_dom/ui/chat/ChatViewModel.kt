package com.custom.rgs_android_dom.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatViewModel(private val chatInteractor: ChatInteractor) : BaseViewModel() {

    private val chatMessagesController = MutableLiveData<ArrayList<ChatMessageModel>>()
    val chatMessageObserver: LiveData<ArrayList<ChatMessageModel>> = chatMessagesController

    private val newMessageController = MutableLiveData<ChatMessageModel>()
    val newMessageObserver: LiveData<ChatMessageModel> = newMessageController

    init {
        chatInteractor.getChatMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    chatMessagesController.value = it
                },
                onError = {
                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.newMessageSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onNext = {
                    newMessageController.value = it
                },
                onError = {

                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onSendMessageClick(newMessage: String){
        chatInteractor.sendMessage(newMessage)
    }

}