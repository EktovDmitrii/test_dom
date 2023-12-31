package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.chat.models.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.io.File

interface ChatRepository {

    fun getChatHistory(channelId: String): Single<List<ChatMessageModel>>

    fun getChannelMembers(channelId: String): Single<List<ChannelMemberModel>>

    fun sendMessage(channelId: String, message: String?, fileIds: List<String>?): Completable

    fun postFileInChat(channelId: String, file: File): Single<ChatFileModel>

    fun setFilesToUpload(files: List<File>)

    fun getFilesToUploadSubject(): PublishSubject<List<File>>

    fun requestLiveKitToken(channelId: String): Single<CallConnectionModel>

    fun connectToWebSocket()

    fun disconnectFromWebSocket()

    fun getWsEventsSubject(): PublishSubject<WsMessageModel<*>>

    suspend fun connectToLiveKitRoom(callJoin: CallJoinModel, callType: CallType, cameraEnabled: Boolean, micEnabled: Boolean)

    fun getRoomInfoSubject(): PublishSubject<RoomInfoModel>

    fun leaveLiveKitRoom()

    fun getActualRoomInfo(): RoomInfoModel?

    fun clearRoomDataOnOpponentDeclined()

    suspend fun enableMic(enable: Boolean)

    suspend fun enableCamera(enable: Boolean)

    suspend fun switchCamera()

    suspend fun switchVideoTrack()

    fun getChatFilePreview(userId: String, fileId: String): Single<String>

    fun loadCases(): Completable

    fun getCasesFlowable(): Flowable<ClientCasesModel>

    fun getMasterOnlineCase(): CaseModel

    fun viewChannel(channelId: String): Completable

    fun notifyTyping(channelId: String): Completable

    fun getCallInfoSubject(): PublishSubject<CallInfoModel>

    fun acceptCall(channelId: String, callId: String): Completable

    fun declineCall(channelId: String, callId: String): Completable

    fun initCallInfo(consultant: ChannelMemberModel?, callType: CallType)

    fun getCallInfo(): CallInfoModel

    fun getCase(channelId: String): Maybe<CaseModel>

    fun getActiveCall(channelId: String): Maybe<ActiveCallModel>

    fun getCasesSingle(): Single<ClientCasesModel>

}