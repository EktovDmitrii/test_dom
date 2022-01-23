package com.custom.rgs_android_dom.ui.purchase_service

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.custom.rgs_android_dom.databinding.FragmentBottomAddAgentBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe

class AddAgentBottomFragment :
    BaseBottomSheetModalFragment<AddAgentViewModel, FragmentBottomAddAgentBinding>() {

    override val TAG: String = "ADD_AGENT_BOTTOM_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.agentCodeEditText.addTextWatcher { code ->
            viewModel.onAgentCodeChanged(code.trim())
        }
        binding.agentPhoneEditText.addOnTextChangedListener { phone, isMaskFilled ->
            viewModel.onAgentPhoneChanged(phone, isMaskFilled)
        }
        binding.saveButton.setOnDebouncedClickListener {
            viewModel.onSaveClick(
                onComplete = {
                    setFragmentResult(
                        "agent_code_request",
                        bundleOf(
                            "code_status_key" to "1",
                            "code_key" to binding.agentCodeEditText.getText().trim()
                        )
                    )
                    dismiss()
                },
                onError = {
                    setFragmentResult(
                        "agent_code_request",
                        bundleOf(
                            "code_status_key" to "0"
                        )
                    )
                    dismiss()
                }
            )
        }

        subscribe(viewModel.isSaveButtonEnabledObserver) {
            binding.saveButton.isEnabled = it
        }
    }

}