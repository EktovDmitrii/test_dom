package com.custom.rgs_android_dom.ui.client

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class ClientFragment(
    peekHeight: Int,
    topMargin: Int,
    maxExpandedHalfRatio: Float,
    minExpandedHalfRatio: Float,
) : BaseBottomSheetFragment<ClientViewModel, FragmentClientBinding>(peekHeight, topMargin, maxExpandedHalfRatio, minExpandedHalfRatio) {

    override val TAG: String = "CLIENT_FRAGMENT"

    override fun getSwipeAnchor(): View? {
        return binding.swipeAnchorLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutRelativeLayout.setOnDebouncedClickListener {
            viewModel.onLogoutClick()
        }

        subscribe(viewModel.clientShortViewStateObserver){state->
            binding.phoneTextView.text = state.phone

            if (state.firstName.isEmpty() || state.lastName.isEmpty()){
                binding.nameTextView.text = "Добавьте ваше имя"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.primary500))
            } else{
                binding.nameTextView.text = "${state.lastName} ${state.firstName}"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.secondary900))
            }
        }
    }


}