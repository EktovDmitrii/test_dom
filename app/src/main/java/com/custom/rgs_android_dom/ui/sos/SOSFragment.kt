package com.custom.rgs_android_dom.ui.sos

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentSosBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.setStatusBarColor

class SOSFragment : BaseFragment<SOSViewModel, FragmentSosBinding>(R.layout.fragment_sos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.chatLinearLayout.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.freePhoneCallLinearLayout.setOnDebouncedClickListener {
            viewModel.onFreePhoneCallClick()
        }

        binding.audioCallLinearLayout.setOnDebouncedClickListener {
            viewModel.onAudioCallClick()
        }

        binding.videoCallLinearLayout.setOnDebouncedClickListener {
            viewModel.onVideoCallClick()
        }

    }

    override fun setStatusBarColor() {
        setStatusBarColor(R.color.isabelline)
    }

}