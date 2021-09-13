package com.custom.rgs_android_dom.ui.chat

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentChatBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

class ChatFragment : BaseFragment<ChatViewModel, FragmentChatBinding>(R.layout.fragment_chat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.secondary100)
    }

}