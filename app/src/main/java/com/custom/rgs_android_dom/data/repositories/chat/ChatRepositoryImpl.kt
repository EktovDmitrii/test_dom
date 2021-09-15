package com.custom.rgs_android_dom.data.repositories.chat

import com.custom.rgs_android_dom.data.network.MSDApi
import com.custom.rgs_android_dom.data.network.mappers.ChatMapper
import com.custom.rgs_android_dom.data.network.requests.SendMessageRequest
import com.custom.rgs_android_dom.data.preferences.AuthSharedPreferences
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.repositories.ChatRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class ChatRepositoryImpl(private val api: MSDApi,
                         private val authSharedPreferences: AuthSharedPreferences
) : ChatRepository {

    override fun getChatMessages(): Single<List<ChatMessageModel>> {
        val client = authSharedPreferences.getClient()
        val channelId = client?.getChatChannelId() ?: ""

        return api.getChatMessages(channelId, 20, 0).map {
            ChatMapper.responseToChatMessages(it, client?.userId ?: "")
        }

//        return Single.fromCallable {
//            val messages = arrayListOf<ChatMessageModel>()
//            messages.add(
//                ChatMessageModel(
//                    message = "Добрый день!",
//                    sender= ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Я мастер онлайн",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Подскажите, чем я могу вам помочь?",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Добрый день, я хотел проконсультироваться у вас по вопросу вызова мастера на дом",
//                    sender = ChatMessageModel.Sender.ME
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Что именно вас интересует",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "У меня течет кран, затопило всю квартиру, соседи ломятся в дверь",
//                    sender = ChatMessageModel.Sender.ME
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Для начала вам надо перекрыть воду",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Я не знаю как это сделать",
//                    sender = ChatMessageModel.Sender.ME
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Найдите люк в котором находятся ваши счестчики воды",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "О, да, тут есть 2 кранчика",
//                    sender = ChatMessageModel.Sender.ME
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Отлично поверните оба",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Да, все получилось, у меня больше не бежит вода",
//                    sender = ChatMessageModel.Sender.ME
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Отлично, теперь я могу вызвать вам на дом мастера, который починит ваш кран",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Да, это было бы отлично, можно так же заказать клининг, что бы прибрали здесь все",
//                    sender = ChatMessageModel.Sender.ME
//                )
//            )
//            messages.add(
//                ChatMessageModel(
//                    message = "Конечно, ожидайте, мастер и клининг уже выехали к вам домой",
//                    sender = ChatMessageModel.Sender.OPPONENT
//                )
//            )
//
//            return@fromCallable messages
//        }.delay(3, TimeUnit.SECONDS)
    }

    override fun sendMessage(message: String): Completable {
        val request = SendMessageRequest(message = message)

        val client = authSharedPreferences.getClient()

        val channelId = client?.getChatChannelId() ?: ""
        val userId = client?.userId ?: ""

        return api.postMessage(userId, channelId, request)
    }
}