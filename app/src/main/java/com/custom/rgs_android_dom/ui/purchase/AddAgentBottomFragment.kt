package com.custom.rgs_android_dom.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentBottomAddAgentBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import java.io.Serializable

class AddAgentBottomFragment :
    BaseBottomSheetModalFragment<AddAgentViewModel, FragmentBottomAddAgentBinding>() {

    override val TAG: String = "ADD_AGENT_BOTTOM_FRAGMENT"
    private var purchaseAgentCodeListener: PurchaseAgentCodeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is PurchaseAgentCodeListener) {
            purchaseAgentCodeListener = parentFragment as PurchaseAgentCodeListener
        }
        return binding.root
    }

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
                    purchaseAgentCodeListener?.onSaveCodeSuccess(binding.agentCodeEditText.getText().trim())
                    dismissAllowingStateLoss()
                },
                onError = {
                    purchaseAgentCodeListener?.onSaveCodeError()
                    dismissAllowingStateLoss()
                }
            )
        }

        subscribe(viewModel.isSaveButtonEnabledObserver) {
            binding.saveButton.isEnabled = it
        }
    }

    interface PurchaseAgentCodeListener : Serializable {
        fun onSaveCodeError()
        fun onSaveCodeSuccess(code: String)
    }
}