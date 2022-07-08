package com.custom.rgs_android_dom.ui.purchase.add.agent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentAddAgentBinding
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.expand
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import com.custom.rgs_android_dom.views.edit_text.MSDMaskedLabelEditText
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

        binding.saveButton.setOnDebouncedClickListener {
            viewModel.onSaveClick()
        }

        binding.agentCodeEditText.addTextWatcher {
            viewModel.onAgentCodeChanged(it)
            binding.agentCodeEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.agentPhoneEditText.addOnTextChangedListener { phone, isMaskFilled ->
            viewModel.onAgentPhoneChanged(phone, isMaskFilled)
            binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
        }

        subscribe(viewModel.agentCodeObserver) {
            purchaseAgentCodeListener?.onSaveCodeSuccess(it)
            onClose()
        }

        subscribe(viewModel.isSaveTextViewEnabledObserver) {
            binding.saveButton.isEnabled = it
        }

        subscribe(viewModel.validateExceptionObserver) { specError ->
            specError.fields.forEach {
                when (it.fieldName) {
                    ClientField.AGENTCODE -> {
                        binding.agentCodeEditText.setState(
                            MSDLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                    ClientField.AGENTPHONE -> {
                        binding.agentPhoneEditText.setState(
                            MSDMaskedLabelEditText.State.ERROR,
                            it.errorMessage
                        )
                    }
                }
            }
        }

        subscribe(viewModel.networkErrorObserver) {
            binding.agentCodeEditText.setState(MSDLabelEditText.State.ERROR, it)
        }
    }

    interface PurchaseAgentListener : Serializable {
        fun onSaveCodeSuccess(code: String)
    }
}
