package com.custom.rgs_android_dom.domain.chat

import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.domain.chat.models.WsChatMessageModel
import com.custom.rgs_android_dom.utils.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.joda.time.LocalDateTime
import java.io.File

class ChatInteractor(
    private val chatRepository: ChatRepository,
    private val authContentProviderManager: AuthContentProviderManager
){
    companion object {
        private const val TYPE_SYSTEM_MESSAGE = "system_"
        private const val TYPE_MEMBER_CONSULTANT = "consultant"
        private const val MAX_UPLOAD_IMAGE_SIZE = 10
    }

    var invalidUploadFileSubject = PublishSubject.create<File>()
    val newChatItemsSubject = PublishSubject.create<List<ChatItemModel>>()
    val callJoinSubject = PublishSubject.create<CallJoinModel>()

    private var cachedChatItems = arrayListOf<ChatItemModel>()
    private var cachedChannelMembers = arrayListOf<ChannelMemberModel>()

    fun getChatHistory(channelId: String): Single<List<ChatItemModel>> {
        return chatRepository.getChatHistory(channelId).map { allMessages->
            val chatItems = arrayListOf<ChatItemModel>()

            val filteredMessages = allMessages.filter { !it.type.startsWith(TYPE_SYSTEM_MESSAGE) }
            if (filteredMessages.isNotEmpty()){
                filteredMessages.forEachIndexed { index, currentMessage ->
                    if (index == 0){
                        val dateDivider = ChatDateDividerModel(currentMessage.createdAt.formatTo(DATE_PATTERN_DAY_MONTH_FULL_ONLY))
                        chatItems.add(dateDivider)
                    } else {
                        val prevMessage = if (index -1 >=0) filteredMessages[index-1] else null
                        val dateDivider = getDateDivider(currentMessage.createdAt, prevMessage?.createdAt)
                        if (dateDivider != null){
                            chatItems.add(dateDivider)
                        }
                    }
                    chatItems.add(currentMessage)
                }
            }
            cachedChatItems = chatItems
            return@map chatItems
        }.flatMap {
            return@flatMap chatRepository.getChannelMembers(channelId)
        }.map {
            cachedChannelMembers.clear()
            cachedChannelMembers.addAll(it)

            cachedChatItems.forEach { chatItem ->
                if (chatItem is ChatMessageModel) {
                    chatItem.member = cachedChannelMembers.find { it.userId == chatItem.userId }
                }
            }

            /*cachedChatItems.forEach { chatItem->
                if (chatItem is ChatMessageModel){
                    chatItem.files?.let { chatFiles->
                        chatFiles.forEach { chatFile->
                            val preview = chatRepository.getChatFilePreview(chatItem.userId, chatFile.id).blockingGet()
                            chatFile.preview = preview
                        }
                    }
                }
            }*/
            return@map cachedChatItems
        }
    }

    fun sendMessage(channelId: String, message: String? = null, fileIds: List<String>? = null): Completable {
        return chatRepository.sendMessage(channelId, message, fileIds)
    }

    fun onFilesToUploadSelected(files: List<File>){
        val bigSizeImageFile = files.find { it.isImage() && it.sizeInMb > MAX_UPLOAD_IMAGE_SIZE}
        if (bigSizeImageFile != null){
            files.forEach {
                it.delete()
            }
            invalidUploadFileSubject.onNext(bigSizeImageFile)
        } else {
            chatRepository.setFilesToUpload(files)
        }
    }

    fun getFilesToUploadSubject(): PublishSubject<List<File>> {
        return chatRepository.getFilesToUploadSubject()
    }

    fun postFilesToChat(channelId: String, files: List<File>): Completable {
        return Observable.fromArray(files)
            .flatMapIterable {
                it
            }.flatMapSingle {
                chatRepository.postFileInChat(channelId, it)
            }.toList()
            .flatMapCompletable { chatFiles ->
                sendMessage(channelId = channelId, message = " ", fileIds = chatFiles.map { it.id })
            }
    }

    fun subscribeToSocketEvents(): Completable {
        return chatRepository.getWsEventsSubject().flatMapCompletable {
            when (it.event){
                WsEventModel.Event.POSTED -> {
                    val messageModel = (it as WsChatMessageModel).data
                    messageModel?.takeIf {!it.type.startsWith(TYPE_SYSTEM_MESSAGE)}?.let { currentMessage ->
                        if (cachedChannelMembers.find { it.userId == currentMessage.userId } == null) {
                            cachedChannelMembers.clear()
                            cachedChannelMembers.addAll(
                                chatRepository.getChannelMembers(currentMessage.channelId).blockingGet()
                            )
                        }
                        onNewMessage(currentMessage)
                    }
                }
                WsEventModel.Event.CALL_JOIN -> {
                    (it as WsCallJoinModel).data?.let { callJoinModel->
                        callJoinSubject.onNext(callJoinModel)
                    }
                }
                WsEventModel.Event.CALL_DECLINED -> {
                    chatRepository.clearRoomDataOnOpponentDeclined()
                }
            }
            Completable.complete()
        }

    }

    fun connectToWebSocket(){
        if (authContentProviderManager.isAuthorized()){
            chatRepository.connectToWebSocket()
        }
    }

    fun requestLiveKitToken(channelId: String): Completable {
        return chatRepository.requestLiveKitToken(channelId)
            .flatMapCompletable {
                Completable.complete()
            }
    }

    suspend fun connectToLiveKitRoom(callJoin: CallJoinModel, callType: CallType, cameraEnabled: Boolean, micEnabled: Boolean){
        chatRepository.connectToLiveKitRoom(callJoin, callType, cameraEnabled, micEnabled)
    }

    fun getRoomInfoSubject(): PublishSubject<RoomInfoModel>{
        return chatRepository.getRoomInfoSubject()
    }

    fun getRoomDisconnectedSubject(): PublishSubject<Unit>{
        return chatRepository.getRoomDisconnectedSubject()
    }

    fun leaveLiveKitRoom(){
        chatRepository.leaveLiveKitRoom()
    }

    fun getActualRoomInfo(): RoomInfoModel?{
        return chatRepository.getActualRoomInfo()
    }

    fun getCallTimeSubject(): Observable<String>{
        return chatRepository.getCallDurationSubject()
            .map {duration->
                return@map duration.toReadableTime()
            }
    }

    suspend fun enableCamera(enable: Boolean){
        chatRepository.enableCamera(enable)
    }

    suspend fun enableMic(enable: Boolean){
        chatRepository.enableMic(enable)
    }

    fun getCurrentConsultant(): ChannelMemberModel? {
        return cachedChannelMembers.find { it.type == TYPE_MEMBER_CONSULTANT }
    }

    private fun onNewMessage(newMessage: ChatMessageModel){
        val chatItems = arrayListOf<ChatItemModel>()
        newMessage.member = cachedChannelMembers.find { it.userId == newMessage.userId }
        // TODO Find out how chat messages can be empty
        if (cachedChatItems.isNotEmpty()){
            val prevMessage = cachedChatItems[cachedChatItems.size-1] as ChatMessageModel
            getDateDivider(newMessage.createdAt, prevMessage.createdAt)?.let { dateDivider->
                chatItems.add(dateDivider)
            }
            chatItems.add(newMessage)
        } else {
            val dateDivider = ChatDateDividerModel(newMessage.createdAt.formatTo(DATE_PATTERN_DAY_MONTH_FULL_ONLY))
            chatItems.add(dateDivider)
            chatItems.add(newMessage)
        }
        cachedChatItems.addAll(chatItems)
        newChatItemsSubject.onNext(chatItems)
    }

    private fun getDateDivider(currentMessageDate: LocalDateTime, previousMessageDate: LocalDateTime?): ChatDateDividerModel? {
        previousMessageDate?.let { previousMessageDate->
            if (previousMessageDate.getPeriod(currentMessageDate).days >0 ) {
                return ChatDateDividerModel(currentMessageDate.formatTo((DATE_PATTERN_DAY_MONTH_FULL_ONLY)))
            }
        }
        return null
    }

    suspend fun switchCamera() {
        chatRepository.switchCamera()
    }

    suspend fun switchVideoTrack() {
        chatRepository.switchVideoTrack()
    }

    fun loadCases(): Completable {
        return chatRepository.loadCases()
    }

    fun getCasesFlowable(): Flowable<ClientCasesModel>{
        return chatRepository.getCasesFlowable()
    }

    fun getUnreadPostsCountFlowable(): Flowable<Int>{
        return chatRepository.getCasesFlowable().map { cases->
            var unreadPosts = 0
            cases.activeCases.forEach {
                unreadPosts += it.unreadPosts
            }
            cases.archivedCases.forEach {
                unreadPosts += it.unreadPosts
            }

            unreadPosts
        }
    }

    fun getMasterOnlineCase(): CaseModel {
        return chatRepository.getMasterOnlineCase()
    }
}