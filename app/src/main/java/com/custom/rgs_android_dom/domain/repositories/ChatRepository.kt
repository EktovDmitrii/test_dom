package com.custom.rgs_android_dom.domain.repositories

import com.custom.rgs_android_dom.domain.chat.models.ChannelMemberModel
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.io.File

interface ChatRepository {

    fun getChatMessages(): Single<List<ChatMessageModel>>

    fun getChannelMembers(): Single<List<ChannelMemberModel>>

    fun sendMessage(message: String?, fileIds: List<String>?): Completable

    fun postFileInChat(file: File): Single<ChatFileModel>

    fun setFilesToUpload(files: List<File>)

    fun getFilesToUploadSubject(): PublishSubject<List<File>>

}