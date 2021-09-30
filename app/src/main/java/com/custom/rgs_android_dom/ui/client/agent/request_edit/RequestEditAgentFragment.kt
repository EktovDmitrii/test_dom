package com.custom.rgs_android_dom.ui.client.agent.request_edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.custom.rgs_android_dom.R
import com.custom.rgs_android_dom.databinding.FragmentRequestEditAgentBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetFragment
import com.custom.rgs_android_dom.utils.*

class RequestEditAgentFragment() : BaseBottomSheetFragment<RequestEditAgentViewModel, FragmentRequestEditAgentBinding>() {

    override val TAG: String = "REQUEST_EDIT_AGENT_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmTextView.setOnDebouncedClickListener {
            viewModel.onConfirmClick()
        }

        binding.cancelTextView.setOnDebouncedClickListener {
            viewModel.onCancelClick()
        }

        binding.successTextView.setOnDebouncedClickListener {
            viewModel.onSuccessClick()
        }

        subscribe(viewModel.isSuccessTextViewVisibleObserver){isVisible->
            binding.successTextView.isVisible = isVisible
            binding.confirmTextView.isVisible = !isVisible
            binding.cancelTextView.isVisible = !isVisible
        }
    }

    override fun onClose() {
        hideSoftwareKeyboard()
        dismissAllowingStateLoss()
    }

    override fun getThemeResource(): Int {
        return R.style.BottomSheet
    }

    override fun onLoading() {
        super.onLoading()
        binding.confirmTextView.setLoading(true)
        binding.cancelTextView.isEnabled = false
    }

    override fun onError() {
        super.onError()
        binding.confirmTextView.setLoading(false)
        binding.cancelTextView.isEnabled = true
    }

    override fun onContent() {
        super.onContent()
        binding.confirmTextView.setLoading(false)
        binding.cancelTextView.isEnabled = true
    }
}