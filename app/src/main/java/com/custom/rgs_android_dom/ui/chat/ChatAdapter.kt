package com.custom.rgs_android_dom.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.custom.rgs_android_dom.databinding.ItemChatMessageMyBinding
import com.custom.rgs_android_dom.databinding.ItemChatMessageOpponentBinding
import com.custom.rgs_android_dom.domain.chat.models.ChatMessageModel

class ChatAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatMessages: ArrayList<ChatMessageModel> = arrayListOf()

    companion object {
        private const val ITEM_TYPE_MY_MESSAGE = 1
        private const val ITEM_TYPE_OPPONENT_MESSAGE = 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages[position]
        when (holder){
            is MyMessageViewHolder -> {
                holder.bind(message)
            }
            is OpponentMessageViewHolder -> {
                holder.bind(message)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (chatMessages[position].sender) {
            ChatMessageModel.Sender.ME -> ITEM_TYPE_MY_MESSAGE
            else -> ITEM_TYPE_OPPONENT_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when (viewType) {
            ITEM_TYPE_MY_MESSAGE -> {
                val binding = ItemChatMessageMyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyMessageViewHolder(binding)
            }
            else -> {
                val binding = ItemChatMessageOpponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OpponentMessageViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    fun setItems(messages: ArrayList<ChatMessageModel>){
        this.chatMessages = messages
        notifyDataSetChanged()
    }

    fun addMessage(newMessage: ChatMessageModel){
        this.chatMessages.add(newMessage)
        notifyItemInserted(chatMessages.size-1)
    }

    inner class MyMessageViewHolder(
        private val binding: ItemChatMessageMyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            binding.messageTextView.text = model.message
        }

    }

    inner class OpponentMessageViewHolder(
        private val binding: ItemChatMessageOpponentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatMessageModel) {
            binding.messageTextView.text = model.message
        }

    }
}