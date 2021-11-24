package com.custom.rgs_android_dom.ui.chat.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.domain.chat.models.ChatItemModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.chat.files.viewers.image.ImageViewerFragment
import com.custom.rgs_android_dom.ui.chat.files.viewers.video.VideoPlayerFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.io.File

class CallViewModel() : BaseViewModel() {

    private val callTypeController = MutableLiveData<CallType>()
    val callTypeObserver: LiveData<CallType> = callTypeController


    fun onBackClick(){
        closeController.value = Unit
    }

    fun onRejectClick() {
        closeController.value = Unit
    }


}