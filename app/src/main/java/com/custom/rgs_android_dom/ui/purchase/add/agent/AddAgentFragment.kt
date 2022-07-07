package com.custom.rgs_android_dom.ui.purchase.add.agent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentAddAgentBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.expand
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.Serializable

class AddAgentFragment : BaseBottomSheetModalFragment<AddAgentViewModel, FragmentAddAgentBinding>() {

    override val TAG: String = "ADD_AGENT_BOTTOM_FRAGMENT"
    private var purchaseAgentCodeListener: PurchaseAgentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is PurchaseAgentListener) {
            purchaseAgentCodeListener = parentFragment as PurchaseAgentListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expand()

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
                    onClose()
                },
                onError = {
                    purchaseAgentCodeListener?.onSaveCodeError()
                    onClose()
                }
            )
        }

        subscribe(viewModel.isSaveButtonEnabledObserver) {
            binding.saveButton.isEnabled = it
        }

        subscribe(viewModel.isKeyboardOpenObserver) {
            val dialog = dialog as BottomSheetDialog
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            isKeyboardOpen = it
        }
    }
}

interface PurchaseAgentListener : Serializable {
    fun onSaveCodeError()
    fun onSaveCodeSuccess(code: String)
}
