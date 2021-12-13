package com.custom.rgs_android_dom.ui.client.agent

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentAgentBinding
import com.custom.rgs_android_dom.ui.base.BaseFragment
import com.custom.rgs_android_dom.ui.client.agent.request_edit.RequestEditAgentFragment
import com.custom.rgs_android_dom.utils.makeStringWithLink
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class AgentFragment : BaseFragment<AgentViewModel, FragmentAgentBinding>(R.layout.fragment_agent) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeRequestLink()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick()
        }

        binding.editImageView.setOnDebouncedClickListener {
            viewModel.onEditClick()
        }

        subscribe(viewModel.agentObserver) { state ->
            binding.editImageView.isVisible = state.isEditAgentButtonVisible

            if (state.agentCode.isNotEmpty()) {
                binding.agentCodeTextView.setValue(state.agentCode)
            } else {
                binding.agentCodeTextView.setNoValue()
            }

            if (state.agentPhone.isNotEmpty()) {
                binding.agentPhoneTextView.setValue(state.agentPhone)
            } else {
                binding.agentPhoneTextView.setNoValue()
            }

            binding.requestEditLinearLayout.isVisible = !state.isRequestEditContainerVisible
            binding.answerEditLinearLayout.isVisible = state.isRequestEditContainerVisible
        }

        subscribe(viewModel.editAgentRequestedObserver) { wasRequested ->
            binding.requestEditLinearLayout.isVisible = !wasRequested
            binding.answerEditLinearLayout.isVisible = wasRequested
        }

    }

    private fun makeRequestLink() {
        binding.requestEditTextView.makeStringWithLink(
            resources.getColor(R.color.primary500,null),
            Pair(
                "оставьте заявку",
                View.OnClickListener {
                    val requestEditAgentFragment = RequestEditAgentFragment()
                    requestEditAgentFragment.show(
                        childFragmentManager,
                        requestEditAgentFragment.TAG
                    )
                })
        )
    }

}