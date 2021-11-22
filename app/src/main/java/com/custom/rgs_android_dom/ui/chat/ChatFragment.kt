package com.custom.rgs_android_dom.ui.chat

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentChatBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.chat.files.upload.UploadFilesFragment
import com.custom.rgs_android_dom.utils.*


class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>(R.layout.fragment_chat) {

    private val chatAdapter: ChatAdapter
        get() = binding.messagesRecyclerView.adapter as ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = binding.messagesRecyclerView.layoutManager as LinearLayoutManager
        layoutManager.stackFromEnd = true
        binding.messagesRecyclerView.layoutManager = layoutManager

        binding.messagesRecyclerView.adapter = ChatAdapter()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.messageEditText.addTextChangedListener {
            binding.sendMessageImageView.isEnabled = it.toString().isNotEmpty()
        }

        binding.sendMessageImageView.setOnDebouncedClickListener {
            viewModel.onSendMessageClick(binding.messageEditText.text.toString().trim())
            binding.messageEditText.text?.clear()
        }

        binding.attachImageView.setOnDebouncedClickListener {
            val uploadFilesFragment = UploadFilesFragment()
            uploadFilesFragment.show(childFragmentManager, uploadFilesFragment.TAG)
        }

        subscribe(viewModel.chatItemsObserver) {
            chatAdapter.setItems(it)
            binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }

        subscribe(viewModel.newItemsObserver) {
            chatAdapter.addNewItems(it)
            binding.messagesRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
        }

        subscribe(viewModel.networkErrorObserver) {
            toast(it)
        }

        binding.messagesRecyclerView.addOnScrollListener(
            ChatAnimator(binding.scrollDownImageView)
        )

        binding.scrollDownImageView.setOnDebouncedClickListener {
            binding.messagesRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }

    }

    override fun onLoading() {
        super.onLoading()
        binding.loadingProgressBar.visible()
        binding.sendMessageBottomAppBar.gone()
    }

    override fun onContent() {
        super.onContent()
        binding.loadingProgressBar.gone()
        binding.sendMessageBottomAppBar.visible()
    }

    override fun onError() {
        super.onError()
        binding.loadingProgressBar.gone()
        binding.sendMessageBottomAppBar.visible()
    }

}