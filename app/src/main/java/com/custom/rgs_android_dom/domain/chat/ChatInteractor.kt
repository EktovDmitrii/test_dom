package com.custom.rgs_android_dom.domain.chat

import com.custom.rgs_android_dom.data.providers.auth.manager.AuthContentProviderManager
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.domain.chat.models.WsChatMessageModel
import com.custom.rgs_android_dom.utils.*
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.joda.time.LocalDateTime
import java.io.File
import java.util.concurrent.TimeUnit

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

    private var cachedChatItems = arrayListOf<ChatItemModel>()
    private var cachedChannelMembers = arrayListOf<ChannelMemberModel>()

    private var typingTimerStarted: Boolean = false
    private var typingIntervalDisposable: Disposable? = null
    private var typingEndedDisposable: Disposable? = null

    fun getChatHistory(channelId: String): Single<List<ChatItemModel>> {
        return chatRepository.getChatHistory(channelId).map { allMessages->
            val chatItems = arrayListOf<ChatItemModel>()

            val filteredMessages = allMessages.filter { !it.type.startsWith(TYPE_SYSTEM_MESSAGE) }
            if (filteredMessages.isNotEmpty()){
                filteredMessages.forEachIndexed { index, currentMessage ->
                    if (index == 0){
                        val dateDivider = ChatDateDividerModel(date = currentMessage.createdAt.formatTo(DATE_PATTERN_DAY_MONTH_FULL_ONLY), channelId = channelId)
                        chatItems.add(dateDivider)
                    } else {
                        val prevMessage = if (index -1 >=0) filteredMessages[index-1] else null
                        val dateDivider = getDateDivider(currentMessage.channelId, currentMessage.createdAt, prevMessage?.createdAt)
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

    fun startListenNewMessageEvent(): Completable {
        return chatRepository.getWsEventsSubject().flatMapCompletable {
            when (it.event){
                WsEvent.POSTED -> {
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

    fun leaveLiveKitRoom(){
        chatRepository.leaveLiveKitRoom()
    }

    fun getActualRoomInfo(): RoomInfoModel?{
        return chatRepository.getActualRoomInfo()
    }


    suspend fun enableCamera(enable: Boolean){
        chatRepository.enableCamera(enable)
    }

    suspend fun enableMic(enable: Boolean){
        chatRepository.enableMic(enable)
    }

    fun getMembersFromRemote(channelId: String): Single<List<ChannelMemberModel>> {
        return chatRepository.getChannelMembers(channelId).map {
            cachedChannelMembers.clear()
            cachedChannelMembers.addAll(it)
            it
        }
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
            getDateDivider(newMessage.channelId, newMessage.createdAt, prevMessage.createdAt)?.let { dateDivider->
                chatItems.add(dateDivider)
            }
            chatItems.add(newMessage)
        } else {
            val dateDivider = ChatDateDividerModel(date = newMessage.createdAt.formatTo(DATE_PATTERN_DAY_MONTH_FULL_ONLY), channelId = newMessage.channelId)
            chatItems.add(dateDivider)
            chatItems.add(newMessage)
        }
        cachedChatItems.addAll(chatItems)
        newChatItemsSubject.onNext(chatItems)
    }

    private fun getDateDivider(channelId: String, currentMessageDate: LocalDateTime, previousMessageDate: LocalDateTime?): ChatDateDividerModel? {
        previousMessageDate?.let { previousMessageDate->
            if (previousMessageDate.getPeriod(currentMessageDate).days >0 ) {
                return ChatDateDividerModel(date = currentMessageDate.formatTo((DATE_PATTERN_DAY_MONTH_FULL_ONLY)), channelId = channelId)
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

    fun viewChannel(channelId: String): Completable {
        return chatRepository.viewChannel(channelId)
    }

    fun notifyTyping(channelId: String){
        if (!typingTimerStarted){
            typingIntervalDisposable = Observable.interval(0,3, TimeUnit.SECONDS)
                .flatMap {
                    chatRepository.notifyTyping(channelId).toObservable<Unit>()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribeBy (
                    onError = {
                        logException(this, it)
                    }
                )
            typingTimerStarted = true
        }

        typingEndedDisposable?.dispose()
        typingEndedDisposable = Completable.timer(3, TimeUnit.SECONDS)
            .subscribe {
                typingTimerStarted = false
                typingIntervalDisposable?.dispose()
            }
    }

    fun getWsEventsSubject(): PublishSubject<WsMessageModel<*>>{
        return chatRepository.getWsEventsSubject()
    }

    fun getCallInfoSubject(): PublishSubject<CallInfoModel>{
        return chatRepository.getCallInfoSubject()
    }

    fun acceptCall(channelId: String, callId: String): Completable {
        return chatRepository.acceptCall(channelId, callId)
    }

    fun declineCall(channelId: String, callId: String): Completable {
        return chatRepository.declineCall(channelId, callId)
    }

    fun initCallInfo(consultant: ChannelMemberModel?, callType: CallType) {
        chatRepository.initCallInfo(consultant, callType)
    }

    fun getCallInfo(): CallInfoModel {
        return chatRepository.getCallInfo()
    }

    fun getCase(channelId: String): Maybe<CaseModel> {
        return chatRepository.getCase(channelId)
    }
}