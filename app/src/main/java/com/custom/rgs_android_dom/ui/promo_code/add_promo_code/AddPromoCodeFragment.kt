package com.custom.rgs_android_dom.ui.promo_code.add_promo_code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddPromoCodeBinding
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.constants.LENGTH_PROMO_CODE
import com.custom.rgs_android_dom.utils.activity.hideKeyboardForced
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class AddPromoCodeFragment :
    BaseBottomSheetModalFragment<AddPromoCodeViewModel, FragmentAddPromoCodeBinding>() {

    companion object {

        private const val KEY_SHOULD_SHOW_AGENT = "KEY_SHOULD_SHOW_AGENT"
        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"

        fun newInstance(
            shouldShowAgentView: Boolean,
            purchaseModel: PurchaseModel?
        ) = AddPromoCodeFragment().args {
            putBoolean(KEY_SHOULD_SHOW_AGENT, shouldShowAgentView)
            if (purchaseModel != null) putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
        }
    }

    override val TAG: String = "ADD_PROMO_CODE_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shouldShowAgentView = requireArguments().getBoolean(KEY_SHOULD_SHOW_AGENT)
        val purchaseModel =
            if (requireArguments().containsKey(ARG_PURCHASE_SERVICE_MODEL)) requireArguments().getSerializable(
                ARG_PURCHASE_SERVICE_MODEL
            ) as PurchaseModel else null

        binding.promoCodeTextInputLayout.addTextWatcher { promoCodeText ->
            viewModel.onPromoCodeChanged(promoCodeText)
        }

        binding.stepTextView.visibleIf(!shouldShowAgentView)
        binding.titleTextView.visibleIf(!shouldShowAgentView)
        binding.saveButtonLayout.visibleIf(shouldShowAgentView)
        binding.actionsFrameLayout.visibleIf(!shouldShowAgentView)

        binding.promoCodeDialogButton.setOnDebouncedClickListener {
            viewModel.onPromoCodeDialogClick(parentFragmentManager, shouldShowAgentView, purchaseModel)
        }

        binding.agentPromoCodeButton.setOnDebouncedClickListener {
            viewModel.onAgentPromoCodeButtonClick(parentFragmentManager, shouldShowAgentView, purchaseModel)
        }

        subscribe(viewModel.promoCodeObserver) {
            binding.promoCodeDialogButton.isEnabled = it.isNotEmpty() && it.length >= LENGTH_PROMO_CODE
            binding.agentPromoCodeButton.isEnabled = it.isNotEmpty() && it.length >= LENGTH_PROMO_CODE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().hideKeyboardForced()
    }
}
