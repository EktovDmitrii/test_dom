package com.custom.rgs_android_dom.ui.chats.call.media_output_chooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.managers.MediaOutputManager

class MediaOutputChooserViewModel(private val mediaOutputManager: MediaOutputManager) : BaseViewModel() {

    private val mediaOutputsController = MutableLiveData<ArrayList<MediaOutputModel>>()
    val mediaOutputsObserver: LiveData<ArrayList<MediaOutputModel>> = mediaOutputsController


    init {
        mediaOutputsController.value = mediaOutputManager.getAvailableMediaOutputs()
    }

    fun onMediaOutputSelected(mediaOutput: MediaOutputModel){
        mediaOutputManager.setMediaOutput(mediaOutput.type, true)
        closeController.value = Unit
    }

}