package com.custom.rgs_android_dom.ui.client

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentClientBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.toast

class ClientFragment(
    peekHeight: Int,
    topMargin: Int,
    maxHalfExpandedRatio: Float,
    minHalfExpandedRatio: Float
) : BaseBottomSheetFragment<ClientViewModel, FragmentClientBinding>(peekHeight, topMargin, maxHalfExpandedRatio, minHalfExpandedRatio) {

    override val TAG: String = "CLIENT_FRAGMENT"

    override fun getSwipeAnchor(): View? {
        return binding.swipeAnchorLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contentNestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY == 0){
                if (isLocked){
                    unlockFromTop()
                }
            } else {
                if (!isLocked){
                    lockToTop()
                }
            }
        }

        binding.logoutRelativeLayout.setOnDebouncedClickListener {
            viewModel.onLogoutClick()
        }

        binding.personalDataRelativeLayout.setOnDebouncedClickListener {
            viewModel.onPersonalDataClick()
        }


        binding.agentInfoLinearLayout.setOnDebouncedClickListener {
            viewModel.onAgentInfoClick()
        }

        binding.aboutAppLinearLayout.setOnDebouncedClickListener {
            viewModel.onAboutAppClick()
        }

        binding.chatLinearLayout.setOnDebouncedClickListener {
            viewModel.onChatClick()
        }

        binding.addPropertyFrameLayout.setOnDebouncedClickListener {
            viewModel.onAddPropertyClick()
        }

        subscribe(viewModel.clientShortViewStateObserver){state->
            binding.phoneTextView.text = state.phone

            if (state.firstName.isEmpty() && state.lastName.isEmpty()){
                binding.nameTextView.text = "Добавьте ваше имя"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.primary500))
            } else{
                binding.nameTextView.text = "${state.lastName} ${state.firstName}"
                binding.nameTextView.setTextColor(requireContext().getColor(R.color.secondary900))
            }
        }

        subscribe(viewModel.networkErrorObserver){
            toast(it)
        }
    }
}