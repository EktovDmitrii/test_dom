package com.custom.rgs_android_dom.ui.chat.files.viewers.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.domain.client.ClientInteractor
import com.custom.rgs_android_dom.domain.client.view_states.PersonalDataViewState
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.client.personal_data.edit.EditPersonalDataFragment
import com.custom.rgs_android_dom.ui.navigation.ScreenManager
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.utils.toHumanReadableDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class VideoPlayerViewModel(private val chatFile: ChatFileModel) : BaseViewModel() {

    private val urlController = MutableLiveData<String>()
    val urlObserver: LiveData<String> = urlController

    private val dateController = MutableLiveData<String>()
    val dateObserver: LiveData<String> = dateController

    private val nameController = MutableLiveData<String>()
    val nameObserver: LiveData<String> = nameController

    private val manageFileController = MutableLiveData<ChatFileModel>()
    val manageFileObserver: LiveData<ChatFileModel> = manageFileController

    init {
        val url = "${BuildConfig.BASE_URL}/api/chat/users/me/files/${chatFile.id}"
        urlController.value = url

        nameController.value = chatFile.name.toUpperCase(Locale.getDefault())

        dateController.value = chatFile.createdAt.toHumanReadableDate()

    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onMoreClick(){
        manageFileController.value = chatFile
    }

}