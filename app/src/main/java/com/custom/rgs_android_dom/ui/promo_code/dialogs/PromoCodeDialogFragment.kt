package com.custom.rgs_android_dom.ui.promo_code.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.custom.rgs_android_dom.databinding.FragmentPromoCodeDialogsBinding
import com.custom.rgs_android_dom.domain.purchase.models.PurchaseModel
import com.custom.rgs_android_dom.domain.translations.TranslationInteractor
import com.custom.rgs_android_dom.ui.base.BaseBottomSheetModalFragment
import com.custom.rgs_android_dom.ui.base.BaseViewModel
import com.custom.rgs_android_dom.ui.constants.PERCENT_PROMO_CODE
import com.custom.rgs_android_dom.ui.constants.SERVICE_PROMO_CODE
import com.custom.rgs_android_dom.utils.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class PromoCodeDialogFragment : BaseBottomSheetModalFragment<PromoCodeDialogViewModel, FragmentPromoCodeDialogsBinding>(){

    companion object {

        private const val KEY_PROMO_CODE_ID = "KEY_PROMO_CODE_ID"
        private const val KEY_SHOULD_SHOW_AGENT = "KEY_SHOULD_SHOW_AGENT"
        private const val ARG_PURCHASE_SERVICE_MODEL = "ARG_PURCHASE_SERVICE_MODEL"

        fun newInstance(
            promoCode: String,
            shouldShowAgentView: Boolean,
            purchaseModel: PurchaseModel?
        ) =
            PromoCodeDialogFragment().args {
                putString(KEY_PROMO_CODE_ID, promoCode)
                putBoolean(KEY_SHOULD_SHOW_AGENT, shouldShowAgentView)
                if (purchaseModel != null) putSerializable(ARG_PURCHASE_SERVICE_MODEL, purchaseModel)
            }
    }

    private var promoCodesListener: PromoCodesListener? = null

    override val TAG: String = "PROMO_CODE_DIALOGS_FRAGMENT"

    override fun getParameters(): ParametersDefinition = {
        parametersOf(
            requireArguments().getString(KEY_PROMO_CODE_ID),
            if (requireArguments().containsKey(ARG_PURCHASE_SERVICE_MODEL)) requireArguments().getSerializable(
                ARG_PURCHASE_SERVICE_MODEL
            ) as PurchaseModel else null,
            requireArguments().getBoolean(KEY_SHOULD_SHOW_AGENT),
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (parentFragment is PromoCodesListener) {
           promoCodesListener = parentFragment as PromoCodesListener
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val durationText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_duration")
        val titleText = TranslationInteractor.getTranslation("app.promo_codes.agent_code_adapter.add_second_title")

        binding.loadingLayout.closeImageView.setOnDebouncedClickListener {
            onClose()
        }

        binding.bindPolicyFailureLayout.chatTextView.setOnDebouncedClickListener {
            viewModel.onChatFailureClick()
        }

        binding.bindPolicyFailureLayout.changeDataTextView.setOnDebouncedClickListener {
            viewModel.onChangeDataFailureClick(parentFragmentManager)
        }

        binding.bindPolicySuccessLayout.understandTextView.setOnDebouncedClickListener {
            viewModel.onPromoCodeClick(parentFragmentManager)
        }

        subscribe(viewModel.getPromoCodesObserver) {
            promoCodesListener?.onGetNewPromoCodesList(it)
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

    interface PromoCodesListener : Serializable {
        fun onGetNewPromoCodesList(isLoadingList: Boolean)
    }
}
