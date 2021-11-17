package com.custom.rgs_android_dom.ui.chat

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.*
import com.custom.rgs_android_dom.domain.chat.models.ChatDateDividerModel
import com.custom.rgs_android_dom.domain.chat.models.ChatItemModel
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.chat.models.Sender
import com.custom.rgs_android_dom.utils.DATE_PATTERN_TIME_ONLY_WITHOUT_SEC
import com.custom.rgs_android_dom.utils.GlideApp
import com.custom.rgs_android_dom.utils.dp
import com.custom.rgs_android_dom.utils.formatTo
import java.sql.Date

class ChatFilesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatItems: ArrayList<ChatItemModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_MY_MESSAGE = 1
        private const val ITEM_TYPE_OPPONENT_MESSAGE = 2
        private const val ITEM_TYPE_DATE_DIVIDER = 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatItems[position]
        when (holder){
            is MyMessageViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }
            is OpponentMessageViewHolder -> {
                holder.bind(message as ChatMessageModel)
            }
            is DateDividerViewHolder -> {
                holder.bind(message as ChatDateDividerModel)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (chatItems[position]) {
            is ChatMessageModel -> {
                if ((chatItems[position] as ChatMessageModel).sender == Sender.ME){
                    ITEM_TYPE_MY_MESSAGE
                } else {
                    ITEM_TYPE_OPPONENT_MESSAGE
                }
            }
            is ChatDateDividerModel -> {
                ITEM_TYPE_DATE_DIVIDER
            }
            else -> {
                ITEM_TYPE_MY_MESSAGE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_MY_MESSAGE -> {
                val binding = ItemChatMessageMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyMessageViewHolder(binding)
            }
            ITEM_TYPE_OPPONENT_MESSAGE -> {
                val binding = ItemChatMessageOpponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OpponentMessageViewHolder(binding)
            }
            ITEM_TYPE_DATE_DIVIDER -> {
                val binding = ItemChatDateDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateDividerViewHolder(binding)
            }
            else -> {
                val binding = ItemChatMessageMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyMessageViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    fun setItems(chatItems: List<ChatItemModel>){
        this.chatItems.clear()
        this.chatItems.addAll(chatItems)
        notifyDataSetChanged()
    }

    fun addNewItems(chatItems: List<ChatItemModel>){
        chatItems.forEach {chatItem->
            this.chatItems.add(chatItem)
            notifyItemInserted(this.chatItems.size-1)
        }

    }

    inner class MyMessageViewHolder(private val binding: ItemChatMessageMyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            binding.messageTextView.text = model.message
            binding.timeTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
        }

    }

    /*inner class MyImageViewHolder(private val binding: ItemChatImageMyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            model.files?.let { files->
                val imageBytes = Base64.decode(files[0].miniPreview, Base64.DEFAULT)
                GlideApp.with(binding.myImageView.context)
                    .load(imageBytes)
                    .transform(CenterCrop(), RoundedCorners(16.dp(binding.myImageView.context)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.myImageView)

                binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
            }
        }
    }

    inner class MyDocumentViewHolder(private val binding: ItemChatDocumentMyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            model.files?.let { files->
                val imageBytes = Base64.decode(files[0].miniPreview, Base64.DEFAULT)
                GlideApp.with(binding.myImageView.context)
                    .load(imageBytes)
                    .transform(CenterCrop(), RoundedCorners(16.dp(binding.myImageView.context)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.myImageView)

                val documentFile =

                binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
            }
        }
    }*/

    inner class OpponentMessageViewHolder(private val binding: ItemChatMessageOpponentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            binding.nameTextView.text = "Мой_Сервис Дом"
            model.member?.let { member->
                binding.nameTextView.text = "${member.firstName} ${member.lastName}, ${member.type}"

                if (member.avatar.isNotEmpty()){
                    GlideApp.with(binding.avatarImageView)
                        .load(GlideUrlProvider.makeAvatarGlideUrl(member.avatar))
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_chat_avatar_stub)
                        .into(binding.avatarImageView)
                } else {
                    binding.avatarImageView.setImageResource(R.drawable.ic_chat_avatar_stub)
                }

            }

            binding.messageTextView.text = model.message
            binding.timeTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
        }
    }

    /*inner class OpponentImageViewHolder(private val binding: ItemChatImageOpponentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            binding.nameTextView.text = "Мой_Сервис Дом"

            model.member?.let { member->
                binding.nameTextView.text = "${member.firstName} ${member.lastName}, ${member.type}"

                if (member.avatar.isNotEmpty()){
                    GlideApp.with(binding.avatarImageView)
                        .load(GlideUrlProvider.makeAvatarGlideUrl(member.avatar))
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_chat_avatar_stub)
                        .into(binding.avatarImageView)
                } else {
                    binding.avatarImageView.setImageResource(R.drawable.ic_chat_avatar_stub)
                }

            }

            model.files?.let { files->
                val imageBytes = Base64.decode(files[0].miniPreview, Base64.DEFAULT)
                GlideApp.with(binding.opponentImageView.context)
                    .load(imageBytes)
                    .transform(CenterCrop(), RoundedCorners(16.dp(binding.opponentImageView.context)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.opponentImageView)

                binding.dateTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
            }
        }
    }*/

    inner class DateDividerViewHolder(private val binding: ItemChatDateDividerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatDateDividerModel) {
            binding.dateTextView.text = model.date
        }
    }
}