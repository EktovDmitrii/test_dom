package com.custom.rgs_android_dom.ui.promo_code.dialogs

import android.os.Bundle
import android.view.View
import com.custom.rgs_android_dom.databinding.FragmentPromoCodeDialogsBinding
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.constants.PERCENT_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.SERVICE_PROMO_CODE
import com.custom.rgs_android_dom.ui.promo_code.add_promo_code.AddPromoCodeFragment
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf

class PromoCodeDialogFragment :
    BaseBottomSheetModalFragment<PromoCodeDialogViewModel, FragmentPromoCodeDialogsBinding>() {

    companion object {

        private const val KEY_PROMO_CODE_ID = "KEY_PROMO_CODE_ID"
        private const val KEY_SHOULD_SHOW_AGENT = "KEY_SHOULD_SHOW_AGENT"

        fun newInstance(promoCodeId: String, shouldShowAgentView: Boolean) =
            PromoCodeDialogFragment().args {
                putString(KEY_PROMO_CODE_ID, promoCodeId)
                putBoolean(KEY_SHOULD_SHOW_AGENT, shouldShowAgentView)
            }
    }

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(KEY_PROMO_CODE_ID)
        )
    }

    override val TAG: String = "PROMO_CODE_DIALOGS_FRAGMENT"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val durationText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_duration")
        val titleText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_second_title")
        val shouldShowAgentView = requireArguments().getBoolean(KEY_SHOULD_SHOW_AGENT)

        binding.loadingLayout.closeImageView.setOnDebouncedClickListener {
            onClose()
        }

        binding.bindPolicyFailureLayout.chatTextView.setOnDebouncedClickListener {
            viewModel.onChatFailureClick()
        }

        binding.bindPolicyFailureLayout.changeDataTextView.setOnDebouncedClickListener {
            val emailBottomFragment = AddPromoCodeFragment.newInstance(shouldShowAgentView)
            emailBottomFragment.show(parentFragmentManager, emailBottomFragment.TAG)
            onClose()
        }

        binding.bindPolicySuccessLayout.understandTextView.setOnDebouncedClickListener {
            viewModel.onPromoCodeClick()
        }

        subscribe(viewModel.promoCodesObserver) { model ->

            val duration = durationText.replace("%@", " ${model.expiredAt?.formatTo(DATE_PATTERN_DATE_ONLY)}")

            binding.bindPolicySuccessLayout.apply {
                when (model.type) {
                    PERCENT_PROMO_CODE -> {
                        itemPercentLayout.apply {
                            root.visible()
                            subtitleTextView.text = model.code
                            titleTextView.text = titleText.replace("%@", "${model.discountInPercent}%")
                            durationTextView.text = duration
                        }
                    }
                    SERVICE_PROMO_CODE -> {
                        itemServiceLayout.apply {
                            root.visible()
                            subtitleTextView.text = model.code
                            titleTextView.text = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_title")
                            durationTextView.text = duration
                        }
                    }
                    else -> {
                        itemSaleLayout.apply {
                            root.visible()
                            subtitleTextView.text = model.code
                            titleTextView.text = titleText.replace("%@", "${model.discountInRubles} Р")
                            durationTextView.text = duration
                        }
                    }
                }
                descriptionTextView.text = model.description
                titleSecondTextView.text = model.name
            }
        }

        subscribe(viewModel.loadingStateObserver) {
            binding.loadingLayout.root.visibleIf(it == BaseViewModel.LoadingState.LOADING)
            binding.bindPolicyFailureLayout.root.visibleIf(it == BaseViewModel.LoadingState.ERROR)
            binding.bindPolicySuccessLayout.root.visibleIf(it == BaseViewModel.LoadingState.CONTENT)
        }
    }
}
