package com.custom.rgs_android_dom.ui.chats.call_request

import android.os.Bundle
import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.data.network.url.GlideUrlProvider
import com.custom.rgs_android_dom.databinding.FragmentRequestCallBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf


class CallRequestFragment : BaseFragment<CallRequestViewModel, FragmentRequestCallBinding>(R.layout.fragment_request_call) {

    companion object {
        private const val ARG_CHAT_CALLER_ID = "ARG_CHAT_CALLER_ID"
        private const val ARG_CHAT_CALL_ID = "ARG_CHAT_CALL_ID"
        private const val ARG_CHAT_CHANNEL_ID = "ARG_CHAT_CHANNEL_ID"

        fun newInstance(callerId: String, callId: String, channelId: String) = CallRequestFragment().args {
            putString(ARG_CHAT_CALLER_ID, callerId)
            putString(ARG_CHAT_CALL_ID, callId)
            putString(ARG_CHAT_CHANNEL_ID, channelId)
        }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(ARG_CHAT_CALLER_ID),
            requireArguments().getString(ARG_CHAT_CALL_ID),
            requireArguments().getString(ARG_CHAT_CHANNEL_ID)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playTune("track6.mp3", true)
        binding.acceptCallImageView.setOnDebouncedClickListener {
            playTune("track4.mp3")
            viewModel.navigateCall()
        }
        binding.declineCallImageView.setOnDebouncedClickListener {
            viewModel.declineCall()
        }
        binding.chatImageView.setOnDebouncedClickListener {
            viewModel.navigateChat()
        }
        subscribe(viewModel.consultantObserver) { consultant ->
            binding.consultantNameTextView.text = "${consultant.lastName} ${consultant.firstName}"

            if (consultant.avatar.isNotEmpty()) {
                GlideApp.with(binding.avatarImageView)
                    .load(GlideUrlProvider.makeHeadersGlideUrl(consultant.avatar))
                    .transform(RoundedCorners(32.dp(requireContext())))
                    .error(R.drawable.ic_call_consultant)
                    .into(binding.avatarImageView)
            } else {
                binding.avatarImageView.setImageResource(R.drawable.ic_call_consultant)
            }
        }
    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.white)
    }

    override fun onClose() {
        if (viewModel.isClosableObserver.value == false)
            hideSoftwareKeyboard()
        else
            super.onClose()
    }
}