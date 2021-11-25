package com.custom.rgs_android_dom.data.repositories.chat

import android.content.Context
import android.util.Log
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.requests.SendMessageRequest
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.utils.WsResponseParser
import com.custom.rgs_android_dom.utils.toMultipartFormData
import com.google.gson.Gson
import io.livekit.android.ConnectOptions
import io.livekit.android.LiveKit
import io.livekit.android.room.Room
import io.livekit.android.room.RoomListener
import io.livekit.android.room.participant.RemoteParticipant
import io.livekit.android.room.track.RemoteTrackPublication
import io.livekit.android.room.track.Track
import io.livekit.android.room.track.TrackPublication
import io.livekit.android.room.track.VideoTrack
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.LocalDateTime
import java.io.File

class ChatRepositoryImpl(private val api: MSDApi,
                         private val clientSharedPreferences: ClientSharedPreferences,
                         private val gson: Gson,
                         private val authContentProviderManager: AuthContentProviderManager,
                         private val context: Context
) : ChatRepository {

    companion object {
        private const val TAG = "MSDWebSocket"

        private const val CLOSE_REASON_NORMAL = 1000
    }

    private val filesToUploadSubject = PublishSubject.create<List<File>>()

    var isConnected = false

    private val wsEventSubject: PublishSubject<WsEventModel<*>> = PublishSubject.create()
    private val wsResponseParser = WsResponseParser(gson)

    private val roomInfoSubject = PublishSubject.create<RoomInfoModel>()
    private val roomDisconnectedSubject = PublishSubject.create<Unit>()

    private var room: Room? = null
    private var myVideoTrack: VideoTrack? = null
    private var opponentVideoTrack: VideoTrack? = null
    private var isInCall: Boolean = false
    private var callType: CallType? = null

    private var webSocket: WebSocket? = null
    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "ON OPEN")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "ON MESSAGE " + text)
            val parsedMessage = wsResponseParser.parse(text, clientSharedPreferences.getClient()?.userId ?: "")
            if (parsedMessage != null){
                wsEventSubject.onNext(parsedMessage)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "ON CLOSED " + reason)
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            Log.d(TAG, "ON failure")
            throwable.printStackTrace()
        }
    }


    private val roomListener = object : RoomListener {

        override fun onTrackSubscribed(
            track: Track,
            publication: TrackPublication,
            participant: RemoteParticipant,
            room: Room
        ) {
            if (track is VideoTrack) {
                opponentVideoTrack = track

                val roomInfo = RoomInfoModel(
                    opponentVideoTrack = opponentVideoTrack
                )
                roomInfoSubject.onNext(roomInfo)
            }
        }

        override fun onDisconnect(room: Room, error: Exception?) {
            super.onDisconnect(room, error)
            clearRoomData()
            roomDisconnectedSubject.onNext(Unit)
        }

        override fun onFailedToConnect(room: Room, error: Exception) {
            super.onFailedToConnect(room, error)
            clearRoomData()
            roomDisconnectedSubject.onNext(Unit)
        }

        override fun onParticipantDisconnected(room: Room, participant: RemoteParticipant) {
            super.onParticipantDisconnected(room, participant)
            // TODO Remove this in the future
            if (room.remoteParticipants.isEmpty()){
                /*clearRoomData()
                roomDisconnectedSubject.onNext(Unit)*/
            }
        }
    }

    override fun connectToWebSocket(){
        val token = authContentProviderManager.getAccessToken()
        Log.d(TAG, "CONNECTING TO SOCKET " + token)
        if (token != null){
            val wsUrl = BuildConfig.WS_URL.replace("%s", token)
            val clientBuilder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                clientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }

            webSocket = clientBuilder
                //.pingInterval(10, TimeUnit.SECONDS)
                .build()
                .newWebSocket(
                    Request.Builder().url(wsUrl).build(),
                    webSocketListener
                )

            isConnected = true

            Log.d(TAG, "CONNECTING")
        }

    }

    override fun disconnectFromWebSocket(){
        isConnected = false
        webSocket?.close(CLOSE_REASON_NORMAL, null)
    }

    override fun getWsEventsSubject(): PublishSubject<WsEventModel<*>> {
        return wsEventSubject
    }

    override fun getChatHistory(): Single<List<ChatMessageModel>> {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""
        return api.getChatMessages(channelId, 1000, 0).map {
            ChatMapper.responseToChatMessages(it, client?.userId ?: "")
        }.map {
            it.reversed()
        }
    }

    override fun getChannelMembers(): Single<List<ChannelMemberModel>> {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""
        return api.getChannelMembers(channelId).map {
            ChatMapper.responseToChannelMembers(it)
        }
    }

    override fun sendMessage(message: String?, fileIds: List<String>?): Completable {
        val request = SendMessageRequest(message = message, fileIds = fileIds)

        val client = clientSharedPreferences.getClient()

        val channelId = client?.getChatChannelId() ?: ""

        return api.postMessage(channelId, request)
    }

    override fun postFileInChat(file: File): Single<ChatFileModel> {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""
        return api.postFileInChat(file.toMultipartFormData(), channelId).map {
            ChatMapper.responseToChatFile(it, client?.id ?: "", LocalDateTime.now())
        }
    }

    override fun getFilesToUploadSubject(): PublishSubject<List<File>> {
        return filesToUploadSubject
    }

    override fun setFilesToUpload(files: List<File>) {
        filesToUploadSubject.onNext(files)
    }

    override fun requestLiveKitToken(): Single<CallInfoModel> {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""
        return api.startCall(channelId).map {
            ChatMapper.responseToCallInfo(it)
        }
    }

    override suspend fun connectToLiveKitRoom(token: String, callType: CallType) {
        val withVideo = callType == CallType.VIDEO_CALL

        this.callType = callType

        room = LiveKit.connect(
            context,
            BuildConfig.LIVEKIT_URL,
            token,
            ConnectOptions(
            ),
            roomListener
        )

        val room = room
        if (room != null){
            val localParticipant = room.localParticipant

            localParticipant.setMicrophoneEnabled(true)
            localParticipant.setCameraEnabled(withVideo)

            val audioTrack = localParticipant.createAudioTrack()
            localParticipant.publishAudioTrack(audioTrack)

            if (withVideo){
                val videoTrack = localParticipant.createVideoTrack()
                localParticipant.publishVideoTrack(videoTrack)
                videoTrack.startCapture()

                myVideoTrack = videoTrack
            }

            clientSharedPreferences.saveLiveKitRoomToken(token)

            val roomInfo = RoomInfoModel(
                callType = callType,
                room = room,
                myVideoTrack = myVideoTrack
            )
            roomInfoSubject.onNext(roomInfo)
            isInCall = true

        }
    }

    override fun getRoomInfoSubject(): PublishSubject<RoomInfoModel> {
        return roomInfoSubject
    }

    override fun leaveLiveKitRoom() {
        room?.disconnect()
        clearRoomData()
    }

    override fun getRoomDisconnectedSubject(): PublishSubject<Unit> {
        return roomDisconnectedSubject
    }

    private fun clearRoomData(){
        room = null
        callType = null
        myVideoTrack = null
        opponentVideoTrack = null
        isInCall = false
        clientSharedPreferences.removeLiveKitRoomToken()
    }


    override fun getActualRoomInfo(): RoomInfoModel? {
        return if (isInCall){
            RoomInfoModel(
                callType = callType,
                room = room,
                myVideoTrack = myVideoTrack,
                opponentVideoTrack = opponentVideoTrack
            )
        } else null
    }
}