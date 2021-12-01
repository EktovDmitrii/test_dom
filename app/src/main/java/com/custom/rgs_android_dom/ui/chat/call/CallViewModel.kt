package com.custom.rgs_android_dom.ui.chat.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.RoomInfoModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CallViewModel(private val callType: CallType,
                    private val chatInteractor: ChatInteractor
) : BaseViewModel() {

    private val callTypeController = MutableLiveData<CallType>()
    val callTypeObserver: LiveData<CallType> = callTypeController

    private val roomInfoController = MutableLiveData<RoomInfoModel>()
    val roomInfoObserver: LiveData<RoomInfoModel> = roomInfoController

    init {
        chatInteractor.subscribeToSocketEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        chatInteractor.callJoinSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    viewModelScope.launch {
                        chatInteractor.connectToLiveKitRoom(it, callType)
                    }
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getRoomInfoSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    roomInfoController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        chatInteractor.getRoomDisconnectedSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    closeController.value = Unit
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        callTypeController.value = callType
    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onRejectClick() {
        chatInteractor.leaveLiveKitRoom()
        closeController.value = Unit
    }

    fun onVideoCallPermissionsGranted(){
        requestLiveKitToken()
    }

    fun onAudioCallPermissionsGranted(){
        requestLiveKitToken()
    }

    private fun requestLiveKitToken(){
        val actualRoomInfo = chatInteractor.getActualRoomInfo()
        if (actualRoomInfo != null){
            actualRoomInfo?.let {
                roomInfoController.value = it
            }
        } else {
            chatInteractor.requestLiveKitToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onError = {
                        logException(this, it)
                    }
                ).addTo(dataCompositeDisposable)
        }
    }


}