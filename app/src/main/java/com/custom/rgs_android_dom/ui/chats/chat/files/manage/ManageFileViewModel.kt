package com.custom.rgs_android_dom.ui.chats.chat.files.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel

class ManageFileViewModel(
    private val chatFile: ChatFileModel
) : BaseViewModel() {

    private val downloadTextController = MutableLiveData<String>()
    val downloadTextObserver: LiveData<String> = downloadTextController

    private val downloadFileController = MutableLiveData<ChatFileModel>()
    val downloadFileObserver: LiveData<ChatFileModel> = downloadFileController

    init {
        if (chatFile.hasPreviewImage) {
            downloadTextController.value = "Скачать"
        } else {
            downloadTextController.value = "Скачать документ"
        }
    }

    fun onDownloadClick(){
        downloadFileController.value = chatFile
    }


}