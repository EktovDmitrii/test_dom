package com.custom.rgs_android_dom.ui.promo_code.add_promo_code

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentAddPromoCodeBinding
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.promo_code.add_agent.AddAgentPromoCodeFragment
import com.custom.rgs_android_dom.ui.promo_code.dialogs.PromoCodeDialogsFragment
import com.custom.rgs_android_dom.utils.args
import com.custom.rgs_android_dom.utils.setOnDebouncedClickListener
import com.custom.rgs_android_dom.utils.subscribe
import com.custom.rgs_android_dom.utils.visibleIf

class AddPromoCodeFragment :
    BaseBottomSheetModalFragment<AddPromoCodeViewModel, FragmentAddPromoCodeBinding>() {

    companion object {

        private const val KEY_WAS_CODE_AGENT = "KEY_WAS_CODE_AGENT"

        fun newInstance(isWasCodeAgent: Boolean) = AddPromoCodeFragment().args {
            putBoolean(KEY_WAS_CODE_AGENT, isWasCodeAgent)
        }
    }

    override val TAG: String = "ADD_PROMO_CODE_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isShowCodeAgentView = requireArguments().getBoolean(KEY_WAS_CODE_AGENT)

        binding.promoCodeTextInputLayout.addTextWatcher { promoCodeText ->
            viewModel.setTextPromoCode(promoCodeText)
        }

        binding.stepTextView.visibleIf(!isShowCodeAgentView)
        binding.titleTextView.visibleIf(!isShowCodeAgentView)
        binding.saveButtonLayout.visibleIf(isShowCodeAgentView)
        binding.actionsFrameLayout.visibleIf(!isShowCodeAgentView)

        binding.firstSaveButton.setOnDebouncedClickListener {
            viewModel.promoCodeObserver.value?.let { promoCodeTest ->
                val dialog = PromoCodeDialogsFragment.newInstance(promoCodeTest, isShowCodeAgentView)
                dialog.show(parentFragmentManager, dialog.TAG)
                onClose()
            }
        }

        binding.secondSaveButton.setOnDebouncedClickListener {
            viewModel.promoCodeObserver.value?.let { promoCodeTest ->
                val dialog = AddAgentPromoCodeFragment.newInstance(promoCodeTest, isShowCodeAgentView)
                dialog.show(parentFragmentManager, dialog.TAG)
                onClose()
            }
        }

        subscribe(viewModel.promoCodeObserver) {
            binding.firstSaveButton.isEnabled = it.trim().isNotEmpty()
            binding.secondSaveButton.isEnabled = it.trim().isNotEmpty()
        }
    }
}
