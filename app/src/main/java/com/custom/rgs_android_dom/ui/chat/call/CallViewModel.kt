package com.custom.rgs_android_dom.ui.chat.call

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.custom.rgs_android_dom.domain.chat.ChatInteractor
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.domain.chat.models.ChannelMemberModel
import com.custom.rgs_android_dom.domain.chat.models.RoomInfoModel
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.utils.logException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CallViewModel(private val callType: CallType,
                    private val consultant: ChannelMemberModel?,
                    private val chatInteractor: ChatInteractor
) : BaseViewModel() {

    private val callTypeController = MutableLiveData<CallType>()
    val callTypeObserver: LiveData<CallType> = callTypeController

    private val roomInfoController = MutableLiveData<RoomInfoModel>()
    val roomInfoObserver: LiveData<RoomInfoModel> = roomInfoController

    private val consultantController = MutableLiveData<ChannelMemberModel>()
    val consultantObserver: LiveData<ChannelMemberModel> = consultantController

    private val callTimeController = MutableLiveData<String>()
    val callTimeObserver: LiveData<String> = callTimeController

    private var micEnabled = false
    private var cameraEnabled = false

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
                        chatInteractor.connectToLiveKitRoom(it, callType, cameraEnabled, micEnabled)
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

        chatInteractor.getCallTimeSubject()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    callTimeController.value = it
                },
                onError = {
                    logException(this, it)
                }
            ).addTo(dataCompositeDisposable)

        callTypeController.value = callType

        consultant?.let {
            consultantController.value = it
        }

    }

    fun onBackClick(){
        closeController.value = Unit
    }

    fun onRejectClick(){
        chatInteractor.leaveLiveKitRoom()
        closeController.value = Unit
    }

    fun onAudioCallPermissionsGranted(granted: Boolean){
        micEnabled = granted
        requestLiveKitToken()
    }

    fun onVideoCallPermissionsGranted(granted: Boolean){
        micEnabled = granted
        cameraEnabled = granted
        requestLiveKitToken()
    }

    fun onEnableMicCall(enable: Boolean){
        viewModelScope.launch {
            chatInteractor.enableMic(enable)
        }
    }

    fun onEnableCameraClick(enable: Boolean){
        viewModelScope.launch {
            chatInteractor.enableCamera(enable)
        }
    }

    fun onMinimizeClick(){
        closeController.value = Unit
    }

    private fun requestLiveKitToken(){
        val actualRoomInfo = chatInteractor.getActualRoomInfo()
        if (actualRoomInfo != null){
            actualRoomInfo.let {
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

    fun onSwitchCameraClick(){
        viewModelScope.launch {chatInteractor.switchCamera()}
    }

    fun onVideoTrackSwitchClick(isSwitched: Boolean){
        roomInfoController.value = roomInfoController.value?.copy(videoTracksSwitched = isSwitched)
    }

}