package com.custom.rgs_android_dom.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentChatBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.countries.CountriesAdapter
import com.custom.rgs_android_dom.utils.*

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>(R.layout.fragment_chat) {

    private val chatAdapter: ChatAdapter
        get() = binding.messagesRecyclerView.adapter as ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.messagesRecyclerView.adapter = ChatAdapter()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.sendMessageImageView.setOnDebouncedClickListener {
            viewModel.onSendMessageClick(binding.messageEditText.text.toString().trim())
            binding.messageEditText.text?.clear()
        }

        subscribe(viewModel.chatMessageObserver){
            chatAdapter.setItems(it)
        }

        subscribe(viewModel.newMessageObserver){
            chatAdapter.addMessage(it)
            binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount-1)
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.secondary100)
    }

}