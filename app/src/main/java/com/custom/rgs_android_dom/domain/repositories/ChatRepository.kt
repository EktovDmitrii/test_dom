package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.chat.models.*
import io.livekit.android.room.Room
import io.livekit.android.room.track.VideoTrack
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
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

    suspend fun connectToLiveKitRoom(callJoin: CallJoinModel, callType: CallType)

    fun getRoomInfoSubject(): PublishSubject<RoomInfoModel>

    fun leaveLiveKitRoom()

    fun getRoomDisconnectedSubject(): PublishSubject<Unit>

    fun getActualRoomInfo(): RoomInfoModel?

    fun clearRoomDataOnOpponentDeclined()

}