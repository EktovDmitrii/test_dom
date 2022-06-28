package com.custom.rgs_android_dom.ui.promo_code.add_agent

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddAgentCodeBinding
import com.custom.rgs_android_dom.domain.client.exceptions.ClientField
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogFragment
import com.custom.rgs_android_dom.utils.args
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
        private const val KEY_SHOULD_SHOW_AGENT = "KEY_WAS_CODE_AGENT"

        fun newInstance(promoCodeId: String, shouldShowAgentView: Boolean) =
            AddAgentPromoCodeFragment().args {
                putString(KEY_PROMO_CODE_ID, promoCodeId)
                putBoolean(KEY_SHOULD_SHOW_AGENT, shouldShowAgentView)
            }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(requireArguments().getString(KEY_PROMO_CODE_ID))
    }

    override val TAG: String = "PROMO_CODE_ADD_AGENT_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shouldShowAgentView = requireArguments().getBoolean(KEY_SHOULD_SHOW_AGENT)

        binding.backImageView.setOnDebouncedClickListener {
            val emailBottomFragment = AddPromoCodeFragment.newInstance(shouldShowAgentView)
            emailBottomFragment.show(parentFragmentManager, emailBottomFragment.TAG)
            onClose()
        }

        binding.saveButton.setOnDebouncedClickListener {
            viewModel.onSaveClick(parentFragmentManager, shouldShowAgentView)
        }

        binding.skipButton.setOnDebouncedClickListener {
            val dialog = PromoCodeDialogFragment.newInstance(viewModel.promoCodeIdString, shouldShowAgentView)
            dialog.show(parentFragmentManager, dialog.TAG)
            onClose()
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
