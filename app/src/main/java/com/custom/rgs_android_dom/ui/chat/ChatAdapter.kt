package com.custom.rgs_android_dom.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.*
import com.custom.rgs_android_dom.domain.chat.models.ChatDateDividerModel
import com.custom.rgs_android_dom.domain.chat.models.ChatItemModel
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel
import com.custom.rgs_android_dom.domain.chat.models.Sender
import com.custom.rgs_android_dom.utils.*
import com.custom.rgs_android_dom.utils.recycler_view.VerticalItemDecoration
import java.sql.Date

class ChatAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            val files = model.files
            if (files != null && files.isNotEmpty()){
                binding.attachedFilesRecyclerView.visible()

                binding.messageContainerLinearLayout.gone()
                binding.timeTextView.gone()

                val adapter = ChatMyFilesAdapter(model.createdAt)
                binding.attachedFilesRecyclerView.adapter = adapter
                binding.attachedFilesRecyclerView.addItemDecoration(
                    VerticalItemDecoration(
                        gap = 12.dp(binding.attachedFilesRecyclerView.context)
                    )
                )

                adapter.setItems(files)

            } else {

                binding.attachedFilesRecyclerView.gone()

                binding.messageContainerLinearLayout.visible()
                binding.timeTextView.visible()

                binding.messageTextView.text = model.message
                binding.timeTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
            }

        }

    }

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
            val files = model.files
            if (files != null && files.isNotEmpty()){
                binding.attachedFilesRecyclerView.visible()

                binding.messageTextView.gone()
                binding.timeTextView.gone()

                val adapter = ChatOpponentFilesAdapter(model.createdAt)
                binding.attachedFilesRecyclerView.adapter = adapter
                binding.attachedFilesRecyclerView.addItemDecoration(
                    VerticalItemDecoration(
                        gap = 12.dp(binding.attachedFilesRecyclerView.context)
                    )
                )

                adapter.setItems(files)

            } else {
                binding.attachedFilesRecyclerView.gone()

                binding.messageTextView.visible()
                binding.timeTextView.visible()

                binding.messageTextView.text = model.message
                binding.timeTextView.text = model.createdAt.formatTo(DATE_PATTERN_TIME_ONLY_WITHOUT_SEC)
            }

        }
    }

    inner class DateDividerViewHolder(private val binding: ItemChatDateDividerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatDateDividerModel) {
            binding.dateTextView.text = model.date
        }
    }
}