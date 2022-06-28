package com.custom.rgs_android_dom.ui.promo_code.add_promo_code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddPromoCodeBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.promo_code.add_agent.AddAgentPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class AddPromoCodeFragment :
    BaseBottomSheetModalFragment<AddPromoCodeViewModel, FragmentAddPromoCodeBinding>() {

    companion object {

        private const val KEY_SHOULD_SHOW_AGENT = "KEY_SHOULD_SHOW_AGENT"

        fun newInstance(shouldShowAgentView: Boolean) = AddPromoCodeFragment().args {
            putBoolean(KEY_SHOULD_SHOW_AGENT, shouldShowAgentView)
        }
    }

    override val TAG: String = "ADD_PROMO_CODE_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shouldShowAgentView = requireArguments().getBoolean(KEY_SHOULD_SHOW_AGENT)

        binding.promoCodeTextInputLayout.addTextWatcher { promoCodeText ->
            viewModel.onPromoCodeChanged(promoCodeText)
        }

        binding.stepTextView.visibleIf(!shouldShowAgentView)
        binding.titleTextView.visibleIf(!shouldShowAgentView)
        binding.saveButtonLayout.visibleIf(shouldShowAgentView)
        binding.actionsFrameLayout.visibleIf(!shouldShowAgentView)

        binding.firstSaveButton.setOnDebouncedClickListener {
            viewModel.promoCodeObserver.value?.let { promoCodeTest ->
                val dialog = PromoCodeDialogFragment.newInstance(promoCodeTest, shouldShowAgentView)
                dialog.show(parentFragmentManager, dialog.TAG)
                onClose()
            }
        }

        binding.secondSaveButton.setOnDebouncedClickListener {
            viewModel.promoCodeObserver.value?.let { promoCodeTest ->
                val dialog = AddAgentPromoCodeFragment.newInstance(promoCodeTest,
                    shouldShowAgentView = shouldShowAgentView
                )
                dialog.show(parentFragmentManager, dialog.TAG)
                onClose()
            }
        }

        subscribe(viewModel.promoCodeObserver) {
            binding.firstSaveButton.isEnabled = it.isNotEmpty()
            binding.secondSaveButton.isEnabled = it.isNotEmpty()
        }
    }
}
