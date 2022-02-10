package com.custom.rgs_android_dom.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.domain.chat.models.ChatItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.catalog.product.ProductFragment
import com.custom.rgs_android_dom.ui.catalog.product.ProductLauncher
import com.custom.rgs_android_dom.ui.chat.call.CallFragment
import com.custom.rgs_android_dom.ui.chat.files.viewers.image.ImageViewerFragment
import com.custom.rgs_android_dom.ui.chat.files.viewers.video.VideoPlayerFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class ChatViewModel(private val chatInteractor: ChatInteractor) : BaseViewModel() {

    private val chatItemsController = MutableLiveData<List<ChatItemModel>>()
    val chatItemsObserver: LiveData<List<ChatItemModel>> = chatItemsController

    private val newItemsController = MutableLiveData<List<ChatItemModel>>()
    val newItemsObserver: LiveData<List<ChatItemModel>> = newItemsController

    private val downloadFileController = MutableLiveData<ChatFileModel>()
    val downloadFileObserver: LiveData<ChatFileModel> = downloadFileController

    init {

        chatInteractor.subscribeToSocketEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        chatInteractor.getChatHistory()
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

                    handleNetworkException(it)

                    loadingStateController.value = LoadingState.ERROR
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.newChatItemsSubject
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

    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onSendMessageClick(newMessage: String){
        chatInteractor.sendMessage(message = newMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { handleNetworkException(it) }
            .subscribeBy()
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
        val callFragment = CallFragment.newInstance(CallType.AUDIO_CALL, chatInteractor.getCurrentConsultant())
        ScreenManager.showScreen(callFragment)
    }

    fun onVideoCallClick() {
        val callFragment = CallFragment.newInstance(CallType.VIDEO_CALL, chatInteractor.getCurrentConsultant())
        ScreenManager.showScreen(callFragment)
    }

    private fun postFilesInChat(files: List<File>){
        chatInteractor.postFilesToChat(files)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {

                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)
    }

    fun onProductClick(productId: String) {
        close()
        ScreenManager.showBottomScreen(ProductFragment.newInstance(ProductLauncher(productId = productId)))
    }
}