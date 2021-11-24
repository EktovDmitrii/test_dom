package com.custom.rgs_android_dom.ui.chat.call

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentCallBinding
import com.custom.rgs_android_dom.domain.chat.models.CallType
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.utils.*

class CallFragment : BaseFragment<CallViewModel, FragmentCallBinding>(R.layout.fragment_chat) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rejectImageView.setOnDebouncedClickListener {
            viewModel.onRejectClick()
        }

        subscribe(viewModel.callTypeObserver) {
            when (it) {
                CallType.AUDIO_CALL -> {
                    binding.videoContainer.visibility = View.GONE
                    binding.titleTextView.text = "Звонок"
                }
                CallType.VIDEO_CALL -> {
                    binding.videoContainer.visibility = View.VISIBLE
                    binding.titleTextView.text = "Консультант"
                }
            }
        }

    }

}