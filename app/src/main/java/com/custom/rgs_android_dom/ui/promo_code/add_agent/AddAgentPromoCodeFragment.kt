package com.custom.rgs_android_dom.ui.promo_code.add_agent

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddAgentCodeBinding
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.expand
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.views.edit_text.MSDLabelEditText
import com.custom.rgs_android_dom.views.edit_text.MSDMaskedLabelEditText
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class AddAgentPromoCodeFragment :
    BaseBottomSheetModalFragment<AddAgentPromoCodeViewModel, FragmentAddAgentCodeBinding>() {

    companion object {

        private const val KEY_PROMO_CODE_ID = "KEY_PROMO_CODE_ID"
        private const val KEY_SHOULD_SHOW_AGENT = "KEY_SHOULD_SHOW_AGENT"
        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"

        fun newInstance(
            promoCode: String,
            shouldShowAgentView: Boolean,
            purchaseModel: PurchaseModel?
        ) =
            AddAgentPromoCodeFragment().args {
                putString(KEY_PROMO_CODE_ID, promoCode)
                putBoolean(KEY_SHOULD_SHOW_AGENT, shouldShowAgentView)
                if (purchaseModel != null) putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
            }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(KEY_PROMO_CODE_ID),
            requireArguments().getBoolean(KEY_SHOULD_SHOW_AGENT),
            if (requireArguments().containsKey(ARG_PURCHASE_SERVICE_MODEL)) requireArguments().getSerializable(
                ARG_PURCHASE_SERVICE_MODEL
            ) as PurchaseModel else null
        )
    }

    override val TAG: String = "PROMO_CODE_ADD_AGENT_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expand()

        binding.backImageView.setOnDebouncedClickListener {
            viewModel.onBackClick(parentFragmentManager)
        }

        binding.saveButton.setOnDebouncedClickListener {
            viewModel.onSaveClick(parentFragmentManager)
        }

        binding.skipButton.setOnDebouncedClickListener {
            viewModel.onSkipClick(parentFragmentManager)
        }

        binding.agentCodeEditText.addTextWatcher {
            viewModel.onAgentCodeChanged(it)
            binding.agentCodeEditText.setState(MSDLabelEditText.State.NORMAL)
        }

        binding.agentPhoneEditText.addOnTextChangedListener { phone, isMaskFilled ->
            viewModel.onAgentPhoneChanged(phone, isMaskFilled)
            binding.agentPhoneEditText.setState(MSDMaskedLabelEditText.State.NORMAL)
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
}
