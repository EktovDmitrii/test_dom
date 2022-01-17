package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.data.network.responses.ChatFilePreviewResponse
import com.custom.rgs_android_dom.domain.chat.models.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.Duration
import java.io.File

interface ChatRepository {

    fun getChatHistory(): Single<List<ChatMessageModel>>

    fun getChannelMembers(): Single<List<ChannelMemberModel>>

    fun sendMessage(message: String?, fileIds: List<String>?): Completable

    fun postFileInChat(file: File): Single<ChatFileModel>

    fun setFilesToUpload(files: List<File>)

    fun getFilesToUploadSubject(): PublishSubject<List<File>>

    fun requestLiveKitToken(): Single<CallInfoModel>

    fun connectToWebSocket()

    fun disconnectFromWebSocket()

    fun getWsEventsSubject(): PublishSubject<WsEventModel<*>>

    suspend fun connectToLiveKitRoom(callJoin: CallJoinModel, callType: CallType, cameraEnabled: Boolean, micEnabled: Boolean)

    fun getRoomInfoSubject(): PublishSubject<RoomInfoModel>

    fun leaveLiveKitRoom()

    fun getRoomDisconnectedSubject(): PublishSubject<Unit>

    fun getActualRoomInfo(): RoomInfoModel?

    fun clearRoomDataOnOpponentDeclined()

    fun getCallDurationSubject(): PublishSubject<Duration>

    suspend fun enableMic(enable: Boolean)

    suspend fun enableCamera(enable: Boolean)

    suspend fun switchCamera()

    suspend fun switchVideoTrack()

    fun getChatFilePreview(userId: String, fileId: String): Single<String>

}