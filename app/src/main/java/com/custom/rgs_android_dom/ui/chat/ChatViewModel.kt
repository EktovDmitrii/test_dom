package com.custom.rgs_android_dom.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toNetworkException
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.web_socket.models.WsMessageModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatViewModel(private val chatInteractor: ChatInteractor) : BaseViewModel() {

    private val chatMessagesController = MutableLiveData<List<ChatMessageModel>>()
    val chatMessageObserver: LiveData<List<ChatMessageModel>> = chatMessagesController

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
                    networkErrorController.value = it.toNetworkException()?.message ?: "Ошибка получения истории чата"
                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getWsNewMessageSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onNext = {
                    if (it is WsMessageModel){
                        it.data?.let { newMessage->
                            newMessageController.value = newMessage
                        }
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onSendMessageClick(newMessage: String){
        chatInteractor.sendMessage(newMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    networkErrorController.value = it.toNetworkException()?.message ?: "Ошибка отправки сообщения"
                }
            ).addTo(dataCompositeDisposable)
    }

}