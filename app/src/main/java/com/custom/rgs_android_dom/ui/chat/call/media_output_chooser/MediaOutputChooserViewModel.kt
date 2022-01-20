package com.custom.rgs_android_dom.ui.chat.call.media_output_chooser

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.models.MediaOutputModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.managers.MediaOutputManager

class MediaOutputChooserViewModel(private val mediaOutputManager: MediaOutputManager) : BaseViewModel() {

    private val mediaOutputsController = MutableLiveData<ArrayList<MediaOutputModel>>()
    val mediaOutputsObserver: LiveData<ArrayList<MediaOutputModel>> = mediaOutputsController


    init {
        Log.d("MyLog", "INIT ")
        mediaOutputManager.getAvailableMediaOutputs {
            Log.d("MyLog", "Models size " + it.size)
            for (model in it){
                Log.d("MyLog", "MODEL NAME " + model.name)
            }
            mediaOutputsController.value = it
        }
    }

    fun onMediaOutputSelected(mediaOutput: MediaOutputModel){
        mediaOutputManager.setMediaOutput(mediaOutput)
        closeController.value = Unit
    }

}