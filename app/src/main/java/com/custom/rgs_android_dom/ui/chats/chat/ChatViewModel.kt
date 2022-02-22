package com.custom.rgs_android_dom.ui.chats.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.chats.chat.call.CallFragment
import com.custom.rgs_android_dom.ui.chats.chat.files.viewers.image.ImageViewerFragment
import com.custom.rgs_android_dom.ui.chats.chat.files.viewers.video.VideoPlayerFragment
import com.custom.rgs_android_dom.ui.navigation.PAYMENT
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.ui.purchase.payments.PaymentWebViewFragment
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class ChatViewModel(
    private val case: CaseModel,
    private val chatInteractor: ChatInteractor,
    clientInteractor: ClientInteractor
) : BaseViewModel() {

    private val caseController = MutableLiveData<CaseModel>()
    val caseObserver: LiveData<CaseModel> = caseController

    private val chatItemsController = MutableLiveData<List<ChatItemModel>>()
    val chatItemsObserver: LiveData<List<ChatItemModel>> = chatItemsController

    private val newItemsController = MutableLiveData<List<ChatItemModel>>()
    val newItemsObserver: LiveData<List<ChatItemModel>> = newItemsController

    private val downloadFileController = MutableLiveData<ChatFileModel>()
    val downloadFileObserver: LiveData<ChatFileModel> = downloadFileController

    private var email: String? = null

    init {
        caseController.value = case

        chatInteractor.startListenNewMessageEvent()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        loadChatHistory()

        chatInteractor.newChatItemsSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onNext = {
                    if (it.isNotEmpty() && it[0].channelId == case.channelId){
                        newItemsController.value = it
                        viewChannel()
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getFilesToUploadSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    postFilesInChat(it)
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        clientInteractor.getPersonalData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { personalData ->
                        email = personalData.email.ifEmpty { null }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getWsEventsSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    when (it.event){
                        WsEvent.SOCKET_CONNECTED -> {
                            loadChatHistory()
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
        chatInteractor.sendMessage(channelId = case.channelId, message = newMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { handleNetworkException(it) }
            .subscribeBy(
                onError = {
                    handleNetworkException(it)
                }
            )
            .addTo(dataCompositeDisposable)
    }

    fun onFileClick(chatFile: ChatFileModel){
        when {
            chatFile.mimeType.contains("image") -> {
                val imageViewerFragment = ImageViewerFragment.newInstance(chatFile)
                ScreenManager.showScreen(imageViewerFragment)
            }
            chatFile.mimeType.contains("video") -> {
                val videoPlayerFragment = VideoPlayerFragment.newInstance(chatFile)
                ScreenManager.showScreen(videoPlayerFragment)
            }
            else -> {
                downloadFileController.value = chatFile
            }
        }

    }

    fun onAudioCallClick() {
        val callFragment = CallFragment.newInstance(chatInteractor.getMasterOnlineCase().channelId, CallType.AUDIO_CALL, chatInteractor.getCurrentConsultant())
        ScreenManager.showScreen(callFragment)
    }

    fun onVideoCallClick() {
        val callFragment = CallFragment.newInstance(chatInteractor.getMasterOnlineCase().channelId, CallType.VIDEO_CALL, chatInteractor.getCurrentConsultant())
        ScreenManager.showScreen(callFragment)
    }

    fun onProductClick(productId: String) {
        ScreenManager.showBottomScreen(ProductFragment.newInstance(ProductLauncher(productId = productId)))
    }

    fun onChatClose(){
        viewChannel()
    }

    fun onUserTyping(){
        chatInteractor.notifyTyping(case.channelId)
    }

    fun onPayClick(paymentUrl: String, productId: String, amount: Int) {
        ScreenManager.showScreenScope(
            PaymentWebViewFragment.newInstance(
                url = paymentUrl ,
                productId = productId,
                email =  email ?: "",
                price = amount.toString()
            ), PAYMENT
        )
    }

    private fun loadChatHistory(){
        chatInteractor.getChatHistory(case.channelId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingStateController.value = LoadingState.LOADING }
            .subscribeBy(
                onSuccess = {
                    loadingStateController.value = LoadingState.CONTENT
                    chatItemsController.value = it

                    viewChannel()
                },
                onError = {
                    logException(this, it)

                    handleNetworkException(it)

                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun postFilesInChat(files: List<File>){
        chatInteractor.postFilesToChat(case.channelId, files)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    private fun viewChannel(){
        chatInteractor.viewChannel(case.channelId)
            .andThen(chatInteractor.loadCases())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }
}