package com.custom.rgs_android_dom.data.repositories.chat

import android.content.Context
import android.util.Log
import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.db.MSDDatabase
import com.custom.rgs_android_dom.data.db.mappers.ChatsDbMapper
import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.requests.SendMessageRequest
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.ui.managers.MSDConnectivityManager
import com.custom.rgs_android_dom.ui.managers.MediaOutputManager
import com.custom.rgs_android_dom.utils.WsResponseParser
import com.custom.rgs_android_dom.utils.logException
import com.custom.rgs_android_dom.utils.toMultipartFormData
import com.google.gson.Gson
import io.livekit.android.ConnectOptions
import io.livekit.android.LiveKit
import io.livekit.android.room.Room
import io.livekit.android.room.RoomListener
import io.livekit.android.room.participant.Participant
import io.livekit.android.room.participant.RemoteParticipant
import io.livekit.android.room.track.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import java.io.File
import java.util.concurrent.TimeUnit

class ChatRepositoryImpl(private val api: MSDApi,
                         private val clientSharedPreferences: ClientSharedPreferences,
                         private val gson: Gson,
                         private val authContentProviderManager: AuthContentProviderManager,
                         private val context: Context,
                         private val mediaOutputManager: MediaOutputManager,
                         private val connectivityManager: MSDConnectivityManager,
                         private val database: MSDDatabase
) : ChatRepository {

    companion object {
        private const val TAG = "MSDWebSocket"
        private const val LIVEKIT_TAG = "MSDLiveKit"

        private const val CLOSE_REASON_NORMAL = 1000
    }

    init {
        connectivityManager.connectivitySubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    if (!it){
                        disconnectFromWebSocket()
                        leaveLiveKitRoom()
                    } else {
                        if (!isConnected){
                            connectToWebSocket()
                        }
                    }
                }
            )
    }

    private val filesToUploadSubject = PublishSubject.create<List<File>>()

    var isConnected = false

    private val wsMessageSubject: PublishSubject<WsMessageModel<*>> = PublishSubject.create()
    private val wsResponseParser = WsResponseParser(gson)

    private val roomInfoSubject = PublishSubject.create<RoomInfoModel>()
    private val roomDisconnectedSubject = PublishSubject.create<Unit>()

    private val callDurationSubject = PublishSubject.create<Duration>()
    private var callStartTime: DateTime? = null
    private var callTimeDisposable: Disposable? = null

    private var isInCall: Boolean = false
    private var roomInfo: RoomInfoModel? = null

    private var socketRecreateDisposable: Disposable? = null

    private var webSocket: WebSocket? = null
    private val webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "ON OPEN")
            wsMessageSubject.onNext(WsConnectionModel(WsEvent.SOCKET_CONNECTED))
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "ON MESSAGE " + text)
            socketRecreateDisposable?.dispose()
            val parsedMessage = wsResponseParser.parse(text, clientSharedPreferences.getClient()?.userId ?: "")
            if (parsedMessage != null){
                wsMessageSubject.onNext(parsedMessage)

                when (parsedMessage.event){
                    WsEvent.CALL_DECLINED -> {
                        clearRoomDataOnOpponentDeclined()
                    }
                    WsEvent.ROOM_CLOSED -> {
                        clearRoomDataOnOpponentDeclined()
                    }
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "ON CLOSED " + reason)
        }

        override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
            Log.d(TAG, "ON failure")
            wsMessageSubject.onNext(WsConnectionModel(WsEvent.SOCKET_DISCONNECTED))
            logException(this, throwable)
            disconnectFromWebSocket()
            leaveLiveKitRoom()
            if (connectivityManager.isInternetConnected()){
                connectToWebSocket()
            }
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
                roomInfo = roomInfo?.copy(consultantVideoTrack = track)

                roomInfo?.let {
                    roomInfoSubject.onNext(it)
                }
            }
        }

        override fun onDisconnect(room: Room, error: Exception?) {
            super.onDisconnect(room, error)
            Log.d(LIVEKIT_TAG, "On on disconnected " + error?.message)
            //clearRoomData()
            //roomDisconnectedSubject.onNext(Unit)
        }

        override fun onFailedToConnect(room: Room, error: Exception) {
            super.onFailedToConnect(room, error)
            Log.d(LIVEKIT_TAG, "On failed to connect " + error.message)

            clearRoomData()
            roomDisconnectedSubject.onNext(Unit)
        }

        override fun onTrackMuted(
            publication: TrackPublication,
            participant: Participant,
            room: Room
        ) {
            super.onTrackMuted(publication, participant, room)

            if (publication.track is VideoTrack) {
                roomInfo = roomInfo?.copy(consultantVideoTrack = null)

                roomInfo?.let {
                    roomInfoSubject.onNext(it)
                }
            }
        }

        override fun onTrackUnmuted(
            publication: TrackPublication,
            participant: Participant,
            room: Room
        ) {
            super.onTrackUnmuted(publication, participant, room)
            if (publication.track is VideoTrack) {
                roomInfo = roomInfo?.copy(consultantVideoTrack = publication.track as VideoTrack)

                roomInfo?.let {
                    roomInfoSubject.onNext(it)
                }
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
        }
    }

    override fun disconnectFromWebSocket(){
        isConnected = false
        webSocket?.close(CLOSE_REASON_NORMAL, null)
        webSocket = null
    }

    override fun getWsEventsSubject(): PublishSubject<WsMessageModel<*>> {
        return wsMessageSubject
    }

    override fun getChatHistory(channelId: String): Single<List<ChatMessageModel>> {
        val client = clientSharedPreferences.getClient()
        return api.getChatMessages(channelId, 1000, 0).map {
            ChatMapper.responseToChatMessages(it, client?.userId ?: "")
        }.map {
            it.reversed()
        }
    }

    override fun getChannelMembers(channelId: String): Single<List<ChannelMemberModel>> {
        return api.getChannelMembers(channelId).toSingle().map {
            ChatMapper.responseToChannelMembers(it)
        }.onErrorResumeNext {
            Single.fromCallable {
                listOf()
            }
        }
    }

    override fun sendMessage(channelId: String, message: String?, fileIds: List<String>?): Completable {
        startSocketRecreateTimer()
        val request = SendMessageRequest(message = message, fileIds = fileIds)
        return api.postMessage(channelId, request)
    }

    override fun postFileInChat(channelId: String, file: File): Single<ChatFileModel> {
        val client = clientSharedPreferences.getClient()
        return api.postFileInChat(file.toMultipartFormData(), channelId).map {
            ChatMapper.responseToChatFile(it, client?.id ?: "", LocalDateTime.now(DateTimeZone.getDefault()))
        }
    }

    override fun getFilesToUploadSubject(): PublishSubject<List<File>> {
        return filesToUploadSubject
    }

    override fun setFilesToUpload(files: List<File>) {
        filesToUploadSubject.onNext(files)
    }

    override fun requestLiveKitToken(channelId: String): Single<CallInfoModel> {
        return api.startCall(channelId).map {
            ChatMapper.responseToCallInfo(it)
        }
    }

    override suspend fun connectToLiveKitRoom(callJoin: CallJoinModel, callType: CallType, cameraEnabled: Boolean, micEnabled: Boolean) {
        mediaOutputManager.onCallStarted()

        val withVideo = (callType == CallType.VIDEO_CALL && cameraEnabled)

        startCallTimer()

        val room = LiveKit.connect(
            context,
            BuildConfig.LIVEKIT_URL,
            callJoin.token,
            ConnectOptions(),
            roomListener
        )

        val videoTrack = if (withVideo){
            val videoTrack = room.localParticipant.createVideoTrack()
            room.localParticipant.publishVideoTrack(videoTrack)
            videoTrack.startCapture()
            videoTrack
        } else {
            null
        }

        val audioTrack = if (micEnabled){
            val audioTrack = room.localParticipant.createAudioTrack()
            audioTrack.enabled = micEnabled
            room.localParticipant.publishAudioTrack(audioTrack)
            audioTrack
        } else {
            null
        }

        roomInfo = RoomInfoModel(
            callType = callType,
            room = room,
            cameraEnabled = cameraEnabled,
            micEnabled = micEnabled,
            myAudioTrack = audioTrack,
            myVideoTrack = videoTrack
        )

        clientSharedPreferences.saveLiveKitRoomCredentials(callJoin)
        isInCall = true

        roomInfo?.let {
            roomInfoSubject.onNext(it)
        }

    }

    override fun getRoomInfoSubject(): PublishSubject<RoomInfoModel> {
        return roomInfoSubject
    }

    override fun leaveLiveKitRoom() {
        if (roomInfo?.room?.state == Room.State.CONNECTED){
            roomInfo?.room?.disconnect()
        }
        clearRoomData()
    }

    override fun getRoomDisconnectedSubject(): PublishSubject<Unit> {
        return roomDisconnectedSubject
    }

    override fun getActualRoomInfo(): RoomInfoModel? {
        return if (isInCall){
            roomInfo
        } else null
    }

    override fun clearRoomDataOnOpponentDeclined() {
        clearRoomData()
        roomDisconnectedSubject.onNext(Unit)
    }

    override fun getCallDurationSubject(): PublishSubject<Duration> {
        return callDurationSubject
    }

    override suspend fun enableCamera(enable: Boolean) {
        roomInfo = roomInfo?.copy(cameraEnabled = enable)

        if (enable && roomInfo?.myVideoTrack == null){
            val videoTrack = roomInfo?.room?.localParticipant?.createVideoTrack()

            videoTrack?.let {videoTrack->
                roomInfo?.room?.localParticipant?.publishVideoTrack(videoTrack)
                videoTrack.startCapture()
            }

            roomInfo = roomInfo?.copy(
                myVideoTrack = videoTrack
            )
        }
        roomInfo?.myVideoTrack?.enabled = enable

        roomInfo?.let {
            roomInfoSubject.onNext(it)
        }
    }

    override suspend fun enableMic(enable: Boolean) {
        roomInfo = roomInfo?.copy(micEnabled = enable)

        if (enable && roomInfo?.myAudioTrack == null){
            val audioTrack =  roomInfo?.room?.localParticipant?.createAudioTrack()

            audioTrack?.let { audioTrack->
                roomInfo?.room?.localParticipant?.publishAudioTrack(audioTrack)
            }
            roomInfo = roomInfo?.copy(myAudioTrack = audioTrack)
        }

        roomInfo?.myAudioTrack?.enabled = enable

        roomInfo?.let {
            roomInfoSubject.onNext(it)
        }
    }

    private fun clearRoomData(){
        roomInfo = null
        isInCall = false
        clientSharedPreferences.clearLiveKitRoomCredentials()
        stopCallTimer()

        mediaOutputManager.onCallEnded()

        /*
        myVideoTrack = null
        opponentVideoTrack = null
        isInCall = false
        */
    }

    private fun startCallTimer(){
        callStartTime = DateTime.now()
        callTimeDisposable = Observable.timer(1000, TimeUnit.MILLISECONDS)
            .repeat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val duration = Duration(callStartTime, DateTime.now())

                callDurationSubject.onNext(duration)
            }
    }

    private fun stopCallTimer(){
        callTimeDisposable?.dispose()
        callStartTime = null
    }

    override suspend fun switchCamera() {
        val room = roomInfo?.room as Room
        val localVideoTrackOptions = room.videoTrackCaptureDefaults
        val myVideoTrackOptions = room.localParticipant.videoTrackCaptureDefaults
        val myVideoTrack = (roomInfo?.myVideoTrack as LocalVideoTrack)
        if (localVideoTrackOptions.position == CameraPosition.FRONT){
            roomInfo = roomInfo?.copy(frontCameraEnabled = false)
            room.localParticipant.videoTrackCaptureDefaults = myVideoTrackOptions.copy(position = CameraPosition.BACK)
        } else {
            roomInfo = roomInfo?.copy(frontCameraEnabled = true)
            room.localParticipant.videoTrackCaptureDefaults = myVideoTrackOptions.copy(position = CameraPosition.FRONT)
        }
        myVideoTrack.restartTrack(room.localParticipant.videoTrackCaptureDefaults)
        roomInfo?.let {
            roomInfoSubject.onNext(it)
        }
    }

    override suspend fun switchVideoTrack() {
        val newValue = roomInfo?.videoTracksSwitched ?: false
        roomInfo = roomInfo?.copy(videoTracksSwitched = !newValue)

        roomInfo?.let {
            roomInfoSubject.onNext(it)
        }
    }

    override fun getChatFilePreview(userId: String, fileId: String): Single<String> {
        return api.getChatFilePreview(userId, fileId).map {
            it.filename ?: ""
        }
    }


    override fun loadCases(): Completable {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""

        return Single.zip(
            api.getCases(size = 5000, index = 0),
            api.getSubtypes(size = 5000, index = 0, withArchived = true, withInternal = true),
            api.getUnreadPostsCount(channelId)){cases, subtypes, unreadPosts->

            val cases = ChatsDbMapper.fromResponse(
                response = cases,
                subtypes = subtypes.subtypes ?: listOf(),
                masterOnlineChannelId = channelId,
                masterOnlineUnreadPosts = unreadPosts.count ?: 0
            )
            database.runInTransaction {
                database.chatsDao.clearCases()
                database.chatsDao.insertCases(cases)
            }
        }.flatMapCompletable {
            Completable.complete()
        }
    }

    override fun getCasesFlowable(): Flowable<ClientCasesModel> {
        return database.chatsDao.getCasesFlowable().map {
            ChatsDbMapper.toModel(it)
        }
    }

    override fun getMasterOnlineCase(): CaseModel {
        val client = clientSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""

        return CaseModel(
            channelId = channelId,
            name = "Мастер онлайн",
            subtype = null,
            taskId = "",
            unreadPosts = 0,
            isArchived = false,
            status = CaseStatus.UNKNOWN,
            subStatus = CaseSubStatus.UNKNOWN,
            reportedAt = DateTime.now()
        )
    }

    override fun viewChannel(channelId: String): Completable {
        return api.viewChannel(channelId)
    }

    override fun notifyTyping(channelId: String): Completable {
        return api.notifyTyping(channelId)
    }

    private fun startSocketRecreateTimer(){
        socketRecreateDisposable?.dispose()
        socketRecreateDisposable = Completable.timer(6, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                Log.d(TAG, "No one responding. Disconnecting")
                disconnectFromWebSocket()
                leaveLiveKitRoom()
                connectToWebSocket()
            }
    }
}