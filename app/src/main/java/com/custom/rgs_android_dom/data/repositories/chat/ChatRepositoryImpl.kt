package com.custom.rgs_android_dom.data.repositories.chat

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.requests.SendMessageRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.data.preferences.ClientSharedPreferences
import com.custom.rgs_android_dom.domain.chat.models.ChannelMemberModel
import com.custom.rgs_android_dom.domain.chat.models.ChatFileModel
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import com.custom.rgs_android_dom.utils.toMultipartFormData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.io.File

class ChatRepositoryImpl(private val api: MSDApi,
                         private val clientSharedPreferences: ClientSharedPreferences
) : ChatRepository {

    private val filesToUploadSubject = PublishSubject.create<List<File>>()

    override fun getChatMessages(): Single<List<ChatMessageModel>> {
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
            ChatMapper.responseToChatFile(it)
        }
    }

    override fun getFilesToUploadSubject(): PublishSubject<List<File>> {
        return filesToUploadSubject
    }

    override fun setFilesToUpload(files: List<File>) {
        filesToUploadSubject.onNext(files)
    }
}