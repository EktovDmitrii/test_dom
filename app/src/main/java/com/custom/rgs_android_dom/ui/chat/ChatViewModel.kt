package com.custom.rgs_android_dom.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.data.network.toNetworkException
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.ChatItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class ChatViewModel(private val chatInteractor: ChatInteractor) : BaseViewModel() {

    private val chatItemsController = MutableLiveData<List<ChatItemModel>>()
    val chatItemsObserver: LiveData<List<ChatItemModel>> = chatItemsController

    private val newItemsController = MutableLiveData<List<ChatItemModel>>()
    val newItemsObserver: LiveData<List<ChatItemModel>> = newItemsController

    init {
        chatInteractor.getChatItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    chatItemsController.value = it
                },
                onError = {
                    logException(this, it)
                    networkErrorController.value = it.toNetworkException()?.message ?: "Ошибка получения истории чата"
                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getNewItemsSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty()){
                        newItemsController.value = it
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